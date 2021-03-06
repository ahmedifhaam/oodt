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


package org.apache.oodt.cas.workflow.webapp.monitor.events;

import org.apache.oodt.cas.webcomponents.workflow.event.EventToWorkflowViewer;
import org.apache.oodt.cas.workflow.webapp.monitor.WMMonitorApp;
import org.apache.oodt.cas.workflow.webapp.monitor.workflow.WorkflowViewerPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * Describe your class here.
 *
 * @author mattmann
 * @version $Revision$
 *
 */
public class WorkflowEventViewerPage extends WebPage {
  
  public WorkflowEventViewerPage(){
    add(new Link("home_link"){
      /* (non-Javadoc)
      * @see org.apache.wicket.markup.html.link.Link#onClick()
      */
     @Override
     public void onClick() {
       setResponsePage(getApplication().getHomePage());
     }
  });    
    add(new EventToWorkflowViewer("viewer", ((WMMonitorApp)getApplication()).getWorkflowUrl(), WorkflowViewerPage.class));
  }

}
