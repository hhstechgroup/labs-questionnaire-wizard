package com.engagepoint.labs.wizard.listener;

import com.engagepoint.labs.wizard.model.NavigationData;
import org.primefaces.context.RequestContext;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

public class UINavigationPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -3163849709829451995L;

    @Inject
    NavigationData modelForController;

    @Override
    public void afterPhase(PhaseEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        if (modelForController.isNeedRefresh()) {
            modelForController.setNeedRefresh(false);
            redirectPage();
//            FacesContext facesContext = event.getFacesContext();
//            HttpServletResponse response = (HttpServletResponse) facesContext
//                    .getExternalContext().getResponse();
//            response.addHeader("Pragma", "no-cache");
//            response.addHeader("Cache-Control", "no-cache");
//            response.setHeader("Cache-Control", "no-store");
//            response.addHeader("Cache-Control", "must-revalidate");
//            response.addHeader("Cache-Control", "max-age=0");
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    private void redirectPage() {
//        HttpServletRequest origRequest = (HttpServletRequest) FacesContext
//                .getCurrentInstance().getExternalContext().getRequest();
//        ExternalContext externalContext = FacesContext.getCurrentInstance()
//                .getExternalContext();
//        try {
//            externalContext.redirect(origRequest.getRequestURI());
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        RequestContext.getCurrentInstance().update("maincontentid-j_id1");
        RequestContext.getCurrentInstance().update("leftmenuid-leftMenu");
        RequestContext.getCurrentInstance().update("brd-breadcrumb");
        RequestContext.getCurrentInstance().update("navigationButtonsForm-btnsDiv");
    }

}
