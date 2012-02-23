/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.examples.clipboard;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;


/**
 * Very basic resource example showcases CRUD functionality
 * implemented via HTTP POST, GET, PUT and DELETE methods.
 * A simple clipboard is simulated which is capable of handling
 * text data only.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 */
@Path("clipboard")
public class ClipboardResource {

    private static final List<String> history = new LinkedList<String>();

    private static ClipboardData content = new ClipboardData("");

    // FIXME: json should not work!
    @GET
    @Produces({"text/plain", "application/json"})
    public Response content() {
        if (content.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(content, "text/plain").build();
    }

    @PUT
    @Consumes({"text/plain", "application/json"})
    public void setContent(ClipboardData newContent) {
        saveHistory();
        updateContent(newContent);
    }

    private static void updateContent(ClipboardData newContent) {
        content = newContent;
    }

    @POST
    @Consumes({"text/plain", "application/json"})
    @Produces({"text/plain", "application/json"})
    public ClipboardData append(ClipboardData appendix) {
        saveHistory();
        return content.append(appendix);
    }

    @DELETE
    public void clear() {
        saveHistory();
        content.clear();
    }

    @GET @Path("history")
    @Produces({"text/plain", "application/json"})
    public List<String> getHistory() {
        return history;
    }

//    @GET @Path("fail")
//    @Produces({"text/plain", "application/json"})
//    public Object getFail() {
//        return new Object() {
//            @Override
//            public String toString() {
//                throw new NullPointerException();
//            }
//        };
//    }

    @DELETE @Path("history")
    public void clearHistory() {
        history.clear();
    }

    private void saveHistory() {
        String currentContent = content.toString();
        if (!currentContent.isEmpty()) {
            history.add(currentContent);
        }
    }
}
