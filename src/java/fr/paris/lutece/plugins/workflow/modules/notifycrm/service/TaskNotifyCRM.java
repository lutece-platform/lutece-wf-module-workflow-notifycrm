/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.crmclient.service.ICRMClientService;
import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskNotifyCRM
 *
 */
public class TaskNotifyCRM extends SimpleTask
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_CRM_NOTIFICATION = "admin/plugins/workflow/modules/notifycrm/task_notify_crm_notification.html";
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskNotifyCRMConfigService;
    @Inject
    private INotifyCRMService _notifyCRMService;
    @Inject
    private ICRMClientService _crmClientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskNotifyCRMConfig config = _taskNotifyCRMConfigService.findByPrimaryKey( getId(  ) );

        if ( ( config != null ) && ( resourceHistory != null ) &&
                Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType(  ) ) )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

            // Record
            Record record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );

            if ( record != null )
            {
                Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory(  ).getIdDirectory(  ),
                        pluginDirectory );

                if ( directory != null )
                {
                    record.setDirectory( directory );

                    String strIdDemand = _notifyCRMService.getIdDemand( config, record.getIdRecord(  ),
                            directory.getIdDirectory(  ) );
                    String strStatusText = config.getStatusText(  );

                    if ( config.getSendNotification(  ) )
                    {
                        Map<String, Object> model = _notifyCRMService.fillModel( config, record, directory, request,
                                getAction(  ).getId(  ), nIdResourceHistory );
                        HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( AppTemplateService.getTemplate( 
                                    TEMPLATE_TASK_NOTIFY_CRM_NOTIFICATION, locale, model ).getHtml(  ), locale, model );

                        String strObject = AppTemplateService.getTemplateFromStringFtl( config.getSubject(  ), locale,
                                model ).getHtml(  );
                        String strMessage = template.getHtml(  );
                        String strSender = config.getSenderName(  );

                        _crmClientService.notify( strIdDemand, strObject, strMessage, strSender, config.getBaseURL(  ) );
                    }

                    _crmClientService.sendUpdateDemand( strIdDemand, strStatusText, config.getBaseURL(  ) );
                }
            }
        }
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        String strTitle = StringUtils.EMPTY;
        TaskNotifyCRMConfig config = _taskNotifyCRMConfigService.findByPrimaryKey( getId(  ) );

        if ( config != null )
        {
            strTitle = config.getSubject(  );
        }

        return StringUtils.isNotBlank( strTitle ) ? strTitle : StringUtils.EMPTY;
    }

    // DO

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig(  )
    {
        _taskNotifyCRMConfigService.remove( getId(  ) );
    }
}
