/*
 * Copyright 2002, 2003,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs.impl;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractVfsComponent;
import org.apache.commons.vfs.provider.FileReplicator;
import org.apache.commons.vfs.provider.TemporaryFileStore;
import org.apache.commons.vfs.util.Messages;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple file replicator and temporary file store.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.2 $ $Date: 2002/07/05 04:08:18 $
 */
public final class DefaultFileReplicator
    extends AbstractVfsComponent
    implements FileReplicator, TemporaryFileStore
{

    private final ArrayList copies = new ArrayList();
    private File tempDir;
    private long filecount;

    public DefaultFileReplicator(final File tempDir)
    {
        this.tempDir = tempDir;
    }

    public DefaultFileReplicator()
    {
    }

    /**
     * Initialises this component.
     */
    public void init() throws FileSystemException
    {
        if (tempDir == null)
        {
            tempDir = new File("vfs_cache").getAbsoluteFile();
        }
        filecount = new Random().nextInt() & 0xffff;
    }

    /**
     * Closes the replicator, deleting all temporary files.
     */
    public void close()
    {
        // Delete the temporary files
        while (copies.size() > 0)
        {
            final File file = (File) copies.remove(0);
            try
            {
                final FileObject fileObject = getContext().toFileObject(file);
                fileObject.delete(Selectors.SELECT_ALL);
            }
            catch (final FileSystemException e)
            {
                final String message = Messages.getString("vfs.impl/delete-temp.warn", file.getName());
                getLogger().warn(message, e);
            }
        }

        // Clean up the temp directory, if it is empty
        if (tempDir != null && tempDir.exists() && tempDir.list().length == 0)
        {
            tempDir.delete();
            tempDir = null;
        }
    }

    /**
     * Allocates a new temporary file.
     */
    public File allocateFile(final String baseName)
    {
        // Create a unique-ish file name
        final String basename = baseName + "_" + filecount + ".tmp";
        filecount++;
        final File file = new File(tempDir, basename);

        // Keep track to delete later
        copies.add(file);

        return file;
    }

    /**
     * Creates a local copy of the file, and all its descendents.
     */
    public File replicateFile(final FileObject srcFile,
                              final FileSelector selector)
        throws FileSystemException
    {
        final String basename = srcFile.getName().getBaseName();
        final File file = allocateFile(basename);

        // Copy from the source file
        final FileObject destFile = getContext().toFileObject(file);
        destFile.copyFrom(srcFile, selector);

        return file;
    }

}
