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
package org.apache.commons.vfs.cache;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FilesCache;

/**
 * <p/>
 * A {@link org.apache.commons.vfs.FilesCache} implementation.<br>
 * This implementation never ever caches a single file.
 * </p>
 * <p/>
 * <b>Notice: if you use resolveFile(uri) multiple times with the same path, the system will always create a new instance.
 * Changes on one instance of this file are not seen by the others.</b>
 * </p>
 *
 * @author <a href="mailto:imario@apache.org">Mario Ivanovits</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/10 20:09:48 $
 */
public class NullFilesCache implements FilesCache
{
    public void putFile(final FileObject file)
    {
    }

    public FileObject getFile(final FileSystem filesystem, final FileName name)
    {
        return null;
    }

    public void clear(FileSystem filesystem)
    {
    }

    public void clear()
    {
    }

    public void removeFile(FileSystem filesystem, FileName name)
    {
    }

    public void accessFile(FileObject file)
    {
    }
}
