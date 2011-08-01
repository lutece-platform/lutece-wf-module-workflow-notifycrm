/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.notifycrm.service;

import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.signrequest.NotifyCRMRequestAuthenticatorService;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * NotifyCRMWebService
 *
 */
public final class NotifyCRMWebService
{
    private static final String BEAN_NOTIFY_CRM_WEBSERVICE = "workflow-notifycrm.notifyCRMWebService";

    /**
     * Private constructor
     */
    private NotifyCRMWebService(  )
    {
    }

    /**
     * Get the instance of the service
     * @return the instance of the service
     */
    public static NotifyCRMWebService getService(  )
    {
        return (NotifyCRMWebService) SpringContextService.getPluginBean( NotifyCRMPlugin.PLUGIN_NAME,
            BEAN_NOTIFY_CRM_WEBSERVICE );
    }

    /**
     * Calls the CRM Notification REST WS to notify an user
     * @param strIdDemand the id demand
     * @param strObject the object
     * @param strMessage the message
     * @param strSender the sender
     */
    public void notify( String strIdDemand, String strObject, String strMessage, String strSender )
    {
        String strCRMNotificationRestWebappUrl = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_WEBSERVICE_REST_CRM_NOTIFICATION_WEBAPP_URL );

        if ( StringUtils.isNotBlank( strCRMNotificationRestWebappUrl ) )
        {
            String strUrl = strCRMNotificationRestWebappUrl + NotifyCRMConstants.URL_REST_NOTIFY;

            // List parameters to post
            Map<String, String> params = new HashMap<String, String>(  );
            params.put( NotifyCRMConstants.PARAMETER_ID_DEMAND, strIdDemand );
            params.put( NotifyCRMConstants.PARAMETER_NOTIFICATION_OBJECT, strObject );
            params.put( NotifyCRMConstants.PARAMETER_NOTIFICATION_MESSAGE, strMessage );
            params.put( NotifyCRMConstants.PARAMETER_NOTIFICATION_SENDER, strSender );

            // List elements to include to the signature
            List<String> listElements = new ArrayList<String>(  );
            listElements.add( strIdDemand );
            listElements.add( strObject );
            listElements.add( strSender );

            try
            {
                HttpAccess httpAccess = new HttpAccess(  );
                httpAccess.doPost( strUrl, params, NotifyCRMRequestAuthenticatorService.getRequestAuthenticator(  ),
                    listElements );
            }
            catch ( HttpAccessException e )
            {
                String strError = "NotifyCRMWebServices - Error connecting to '" + strUrl + "' : ";
                AppLogService.error( strError + e.getMessage(  ), e );
                throw new AppException( e.getMessage(  ), e );
            }
        }
        else
        {
            throw new AppException( 
                "NotifyCRMWebService - Could not notify the demand : The property file 'workflow-notifycrm.properties' is not well configured." );
        }
    }

    /**
     * Calls the CRM Notification REST WS to update a demand
     * @param strIdDemand the id demand
     * @param strStatusText the status text
     */
    public void sendUpdateDemand( String strIdDemand, String strStatusText )
    {
        String strCRMNotificationRestWebappUrl = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_WEBSERVICE_REST_CRM_NOTIFICATION_WEBAPP_URL );

        if ( StringUtils.isNotBlank( strCRMNotificationRestWebappUrl ) )
        {
            String strUrl = strCRMNotificationRestWebappUrl + NotifyCRMConstants.URL_REST_UPDATE_DEMAND;

            // List parameters to post
            Map<String, String> params = new HashMap<String, String>(  );
            params.put( NotifyCRMConstants.PARAMETER_ID_DEMAND, strIdDemand );
            params.put( NotifyCRMConstants.PARAMETER_ID_STATUS_CRM, StringUtils.EMPTY );
            params.put( NotifyCRMConstants.PARAMETER_STATUS_TEXT, strStatusText );
            params.put( NotifyCRMConstants.PARAMETER_DEMAND_DATA, StringUtils.EMPTY );

            // List elements to include to the signature
            List<String> listElements = new ArrayList<String>(  );
            listElements.add( strIdDemand );
            listElements.add( StringUtils.EMPTY );
            listElements.add( strStatusText );
            listElements.add( StringUtils.EMPTY );

            try
            {
                HttpAccess httpAccess = new HttpAccess(  );
                httpAccess.doPost( strUrl, params, NotifyCRMRequestAuthenticatorService.getRequestAuthenticator(  ),
                    listElements );
            }
            catch ( HttpAccessException e )
            {
                String strError = "NotifyCRMWebServices - Error connecting to '" + strUrl + "' : ";
                AppLogService.error( strError + e.getMessage(  ), e );
                throw new AppException( e.getMessage(  ), e );
            }
        }
        else
        {
            throw new AppException( 
                "NotifyCRMWebService - Could not update the demand : The property file 'workflow-notifycrm.properties' is not well configured." );
        }
    }
}
