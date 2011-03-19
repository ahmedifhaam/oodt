/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oodt.cas.filemgr.versioning;

//JDK imports
import java.io.File;

//OODT imports
import org.apache.oodt.cas.filemgr.metadata.CoreMetKeys;
import org.apache.oodt.cas.filemgr.structs.Product;
import org.apache.oodt.cas.filemgr.structs.Reference;
import org.apache.oodt.cas.metadata.Metadata;

//Junit imports
import junit.framework.TestCase;

/**
 * 
 * Test harness for the {@link DirectoryProductVersioner}.
 * 
 * @author mattmann
 * @version $Revision$
 * 
 */
public class TestDirectoryBasedProductVersioner extends TestCase {

  public TestDirectoryBasedProductVersioner() {
    System.setProperty("org.apache.oodt.cas.filemgr.mime.type.repository",
        new File("./src/main/resources/mime-types.xml").getAbsolutePath());
  }

  public void testVersioner() {
    DirectoryProductVersioner versioner = new DirectoryProductVersioner();
    Product p = Product.getDefaultFlatProduct("test", "urn:oodt:GenericFile");
    p.getProductType().setProductRepositoryPath("file:///home/files");
    Reference r = new Reference("file:///tmp/dir1", null, 4L);
    Reference r2 = new Reference("file:///tmp/dir1/file1.txt", null, 20L);
    p.getProductReferences().add(r);
    p.getProductReferences().add(r2);
    Metadata met = new Metadata();
    met.addMetadata(CoreMetKeys.FILENAME, "dir1");
    try {
      versioner.createDataStoreReferences(p, met);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    assertNotNull(p.getProductReferences());
    assertEquals(2, p.getProductReferences().size());
    assertEquals("file:/home/files/dir1/", p.getProductReferences().get(0)
        .getDataStoreReference());
    assertEquals("file:/home/files/dir1/file1.txt", p.getProductReferences()
        .get(1).getDataStoreReference());

  }

}
