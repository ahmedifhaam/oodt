//Copyright (c) 2008, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//$Id$

package gov.nasa.jpl.oodt.cas.workflow.lifecycle;

//OODT imports
import gov.nasa.jpl.oodt.cas.commons.xml.DOMUtil;
import gov.nasa.jpl.oodt.cas.commons.xml.XMLUtils;

//JDK imports
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A reader for the <code>workflow-lifecycle.xml</code> file.
 * </p>.
 */
public final class WorkflowLifecyclesReader implements WorkflowLifecycleMetKeys {

    /* our log stream */
    private static Logger LOG = Logger.getLogger(WorkflowLifecyclesReader.class
            .getName());

    private WorkflowLifecyclesReader() throws InstantiationException {
        throw new InstantiationException("Don't construct utility classes!");
    }

    public static List parseLifecyclesFile(String lifecyclesFilePath)
            throws Exception {
        Document doc = getDocumentRoot(lifecyclesFilePath);
        Element rootElem = doc.getDocumentElement();
        List lifecycles = new Vector();

        // make sure that there is a default tag
        Element defaultElem = DOMUtil.getFirstElement(rootElem,
                DEFAULT_LIFECYCLE);

        if (defaultElem == null) {
            throw new Exception("file: [" + lifecyclesFilePath
                    + "] must specify a default workflow lifecycle!");
        }

        WorkflowLifecycle defaultLifecycle = readLifecycle(defaultElem, true);
        lifecycles.add(defaultLifecycle);

        NodeList lifecycleNodes = defaultElem
                .getElementsByTagName(LIFECYCLE_TAG_NAME);
        if (lifecycleNodes != null && lifecycleNodes.getLength() > 0) {
            for (int i = 0; i < lifecycleNodes.getLength(); i++) {
                Element lifecycleElem = (Element) lifecycleNodes.item(i);
                lifecycles.add(readLifecycle(lifecycleElem));
            }
        }

        return lifecycles;

    }

    private static WorkflowLifecycle readLifecycle(Element lifecycleElem) {
        return readLifecycle(lifecycleElem, false);
    }

    private static WorkflowLifecycle readLifecycle(Element lifecycleElem,
            boolean isDefault) {
        WorkflowLifecycle lifecycle = new WorkflowLifecycle();
        String lifecycleName = isDefault ? WorkflowLifecycle.DEFAULT_LIFECYCLE
                : lifecycleElem.getAttribute(LIFECYCLE_TAG_NAME_ATTR);
        lifecycle.setName(lifecycleName);
        lifecycle.setWorkflowId(WorkflowLifecycle.NO_WORKFLOW_ID);
        addStagesToLifecycle(lifecycle, lifecycleElem);

        return lifecycle;
    }

    private static void addStagesToLifecycle(WorkflowLifecycle lifecycle,
            Element lifecycleElem) {
        NodeList stagesNodes = lifecycleElem
                .getElementsByTagName(STAGE_ELEM_NAME);

        if (stagesNodes != null && stagesNodes.getLength() > 0) {
            for (int i = 0; i < stagesNodes.getLength(); i++) {
                Element stageElem = (Element) stagesNodes.item(i);
                WorkflowLifecycleStage stage = new WorkflowLifecycleStage();
                stage.setName(STAGE_TAG_NAME_ATTR);
                stage.setOrder(i+1);
                stage.setStates(XMLUtils.readMany(stageElem, STATUS_TAG_NAME));
                lifecycle.addStage(stage);
            }
        }
    }

    private static Document getDocumentRoot(String xmlFile) {
        // open up the XML file
        DocumentBuilderFactory factory = null;
        DocumentBuilder parser = null;
        Document document = null;
        InputSource inputSource = null;

        InputStream xmlInputStream = null;

        try {
            xmlInputStream = new File(xmlFile).toURL().openStream();
        } catch (IOException e) {
            LOG.log(Level.WARNING,
                    "IOException when getting input stream from [" + xmlFile
                            + "]: returning null document root");
            return null;
        }

        inputSource = new InputSource(xmlInputStream);

        try {
            factory = DocumentBuilderFactory.newInstance();
            parser = factory.newDocumentBuilder();
            document = parser.parse(inputSource);
        } catch (Exception e) {
            LOG.warning("Unable to parse xml file [" + xmlFile + "]."
                    + "Reason is [" + e + "]");
            return null;
        }

        return document;
    }

}
