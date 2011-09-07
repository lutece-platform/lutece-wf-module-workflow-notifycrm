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
package fr.paris.lutece.plugins.workflow.modules.notifycrm.business;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.ResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.business.task.Task;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.NotifyCRMService;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.NotifyCRMWebService;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.TaskNotifyCRMConfigService;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflow.service.security.WorkflowUserAttributesManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskNotifyCRM
 *
 */
public class TaskNotifyCRM extends Task
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_CRM_CONFIG = "admin/plugins/workflow/modules/notifycrm/task_notify_crm_config.html";
    private static final String TEMPLATE_TASK_NOTIFY_CRM_NOTIFICATION = "admin/plugins/workflow/modules/notifycrm/task_notify_crm_notification.html";

    /**
     * {@inheritDoc}
     */
    public void init(  )
    {
    }

    /**
     * {@inheritDoc}
     */
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        NotifyCRMService notifyCRMService = NotifyCRMService.getService(  );
        ResourceHistory resourceHistory = ResourceHistoryHome.findByPrimaryKey( nIdResourceHistory, plugin );
        TaskNotifyCRMConfig config = TaskNotifyCRMConfigService.getService(  ).findByPrimaryKey( getId(  ) );

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

                    String strIdDemand = notifyCRMService.getIdDemand( config, record.getIdRecord(  ),
                            directory.getIdDirectory(  ) );
                    String strStatusText = config.getStatusText(  );

                    if ( config.getSendNotification(  ) )
                    {
                        Map<String, Object> model = notifyCRMService.fillModel( config, record, directory, request,
                                getAction(  ).getId(  ), nIdResourceHistory );
                        HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( AppTemplateService.getTemplate( 
                                    TEMPLATE_TASK_NOTIFY_CRM_NOTIFICATION, locale, model ).getHtml(  ), locale, model );

                        String strObject = AppTemplateService.getTemplateFromStringFtl( config.getSubject(  ), locale,
                                model ).getHtml(  );
                        String strMessage = template.getHtml(  );
                        String strSender = config.getSenderName(  );

                        NotifyCRMWebService.getService(  ).notify( strIdDemand, strObject, strMessage, strSender );
                    }

                    NotifyCRMWebService.getService(  ).sendUpdateDemand( strIdDemand, strStatusText );
                }
            }
        }
    }

    // GET

    /**
     * {@inheritDoc}
     */
    public String getDisplayConfigForm( HttpServletRequest request, Plugin plugin, Locale locale )
    {
        NotifyCRMService notifyCRMService = NotifyCRMService.getService(  );
        TaskNotifyCRMConfigService configService = TaskNotifyCRMConfigService.getService(  );

        String strDefaultSenderName = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_DEFAULT_SENDER_NAME );
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( NotifyCRMConstants.MARK_CONFIG, configService.findByPrimaryKey( getId(  ) ) );
        model.put( NotifyCRMConstants.MARK_DEFAULT_SENDER_NAME, strDefaultSenderName );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_ID_DEMAND,
            notifyCRMService.getListEntriesIdDemand( getId(  ), locale ) );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_USER_GUID,
            notifyCRMService.getListEntriesUserGuid( getId(  ), locale ) );
        model.put( NotifyCRMConstants.MARK_LIST_DIRECTORIES, notifyCRMService.getListDirectories(  ) );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_FREEMARKER,
            notifyCRMService.getListEntriesFreemarker( getId(  ) ) );
        model.put( NotifyCRMConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( NotifyCRMConstants.MARK_LOCALE, request.getLocale(  ) );
        model.put( NotifyCRMConstants.MARK_IS_USER_ATTRIBUTE_WS_ACTIVE,
            WorkflowUserAttributesManager.getManager(  ).isEnabled(  ) );
        model.put( NotifyCRMConstants.MARK_PLUGIN_WORKFLOW, pluginWorkflow );
        model.put( NotifyCRMConstants.MARK_LOCALE, locale );
        model.put( NotifyCRMConstants.MARK_TASKS_LIST, notifyCRMService.getListTasks( getAction(  ).getId(  ), locale ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_CRM_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
        Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList getTaskFormEntries( Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitle( Plugin plugin, Locale locale )
    {
        String strTitle = StringUtils.EMPTY;
        TaskNotifyCRMConfig config = TaskNotifyCRMConfigService.getService(  ).findByPrimaryKey( getId(  ) );

        if ( config != null )
        {
            strTitle = config.getSubject(  );
        }

        return strTitle;
    }

    // DO

    /**
     * {@inheritDoc}
     */
    public void doRemoveConfig( Plugin plugin )
    {
        TaskNotifyCRMConfigService.getService(  ).remove( getId(  ) );
    }

    /**
     * {@inheritDoc}
     */
    public void doRemoveTaskInformation( int nIdHistory, Plugin plugin )
    {
    }

    /**
     * {@inheritDoc}
     */
    public String doSaveConfig( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        TaskNotifyCRMConfigService configService = TaskNotifyCRMConfigService.getService(  );
        String strError = checkNotifyCRMConfigParameter( request, locale );

        if ( StringUtils.isBlank( strError ) )
        {
            // Fetch parameters
            String strIdDirectory = request.getParameter( NotifyCRMConstants.PARAMETER_ID_DIRECTORY );
            String strPositionEntryDirectoryIdDemand = request.getParameter( NotifyCRMConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_ID_DEMAND );
            String strPositionEntryDirectoryUserGuid = request.getParameter( NotifyCRMConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_USER_GUID );
            String strSendNotification = request.getParameter( NotifyCRMConstants.PARAMETER_SEND_NOTIFICATION );
            String strSenderName = request.getParameter( NotifyCRMConstants.PARAMETER_SENDER_NAME );
            String strSubject = request.getParameter( NotifyCRMConstants.PARAMETER_SUBJECT );
            String strMessage = request.getParameter( NotifyCRMConstants.PARAMETER_MESSAGE );
            String strStatusText = request.getParameter( NotifyCRMConstants.PARAMETER_STATUS_TEXT );

            int nIdDirectory = DirectoryUtils.CONSTANT_ID_NULL;
            int nPositionEntryDirectoryIdDemand = DirectoryUtils.CONSTANT_ID_NULL;
            boolean bSendNotification = StringUtils.isNotBlank( strSendNotification );

            if ( StringUtils.isNotBlank( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
            {
                nIdDirectory = Integer.parseInt( strIdDirectory );
            }

            if ( StringUtils.isNotBlank( strPositionEntryDirectoryIdDemand ) &&
                    StringUtils.isNumeric( strPositionEntryDirectoryIdDemand ) )
            {
                nPositionEntryDirectoryIdDemand = Integer.parseInt( strPositionEntryDirectoryIdDemand );
            }

            // In case there are no errors, then the config is created/updated
            boolean bCreate = false;
            TaskNotifyCRMConfig config = configService.findByPrimaryKey( getId(  ) );

            if ( config == null )
            {
                config = new TaskNotifyCRMConfig(  );
                config.setIdTask( getId(  ) );
                bCreate = true;
            }

            config.setIdDirectory( nIdDirectory );
            config.setPositionEntryDirectoryIdDemand( nPositionEntryDirectoryIdDemand );

            if ( StringUtils.isNotBlank( strPositionEntryDirectoryUserGuid ) &&
                    StringUtils.isNumeric( strPositionEntryDirectoryUserGuid ) )
            {
                int nPositionEntryDirectoryUserGuid = Integer.parseInt( strPositionEntryDirectoryUserGuid );
                config.setPositionEntryDirectoryUserGuid( nPositionEntryDirectoryUserGuid );
            }
            else
            {
                config.setPositionEntryDirectoryUserGuid( DirectoryUtils.CONSTANT_ID_NULL );
            }

            config.setSendNotification( bSendNotification );
            config.setMessage( StringUtils.isNotBlank( strMessage ) ? strMessage : StringUtils.EMPTY );
            config.setSenderName( StringUtils.isNotBlank( strSenderName ) ? strSenderName : StringUtils.EMPTY );
            config.setSubject( StringUtils.isNotBlank( strSubject ) ? strSubject : StringUtils.EMPTY );
            config.setStatusText( StringUtils.isNotBlank( strStatusText ) ? strStatusText : StringUtils.EMPTY );

            if ( bCreate )
            {
                configService.create( config );
            }
            else
            {
                configService.update( config );
            }
        }

        return strError;
    }

    /**
     * {@inheritDoc}
     */
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
        Plugin plugin )
    {
        return null;
    }

    // CHECK

    /**
     * {@inheritDoc}
     */
    public boolean isConfigRequire(  )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFormTaskRequire(  )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTaskForActionAutomatic(  )
    {
        return true;
    }

    /**
     * Check if the config is well configured
     * @param request the HTTP request
     * @param locale the Locale
     * @return null if it is well configured, the label of the field that is not well configured otherwise
     */
    private String checkNotifyCRMConfigParameter( HttpServletRequest request, Locale locale )
    {
        String strError = null;

        // Fetch parameters
        String strIdDirectory = request.getParameter( NotifyCRMConstants.PARAMETER_ID_DIRECTORY );
        String strPositionEntryDirectoryIdDemand = request.getParameter( NotifyCRMConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_ID_DEMAND );
        String strSendNotification = request.getParameter( NotifyCRMConstants.PARAMETER_SEND_NOTIFICATION );
        String strSenderName = request.getParameter( NotifyCRMConstants.PARAMETER_SENDER_NAME );
        String strSubject = request.getParameter( NotifyCRMConstants.PARAMETER_SUBJECT );
        String strMessage = request.getParameter( NotifyCRMConstants.PARAMETER_MESSAGE );
        String strApply = request.getParameter( NotifyCRMConstants.PARAMETER_APPLY );
        String strStatusText = request.getParameter( NotifyCRMConstants.PARAMETER_STATUS_TEXT );

        int nIdDirectory = DirectoryUtils.CONSTANT_ID_NULL;
        int nPositionEntryDirectoryIdDemand = DirectoryUtils.CONSTANT_ID_NULL;
        boolean bSendNotification = StringUtils.isNotBlank( strSendNotification );

        if ( StringUtils.isNotBlank( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
        {
            nIdDirectory = Integer.parseInt( strIdDirectory );
        }

        if ( StringUtils.isNotBlank( strPositionEntryDirectoryIdDemand ) &&
                StringUtils.isNumeric( strPositionEntryDirectoryIdDemand ) )
        {
            nPositionEntryDirectoryIdDemand = Integer.parseInt( strPositionEntryDirectoryIdDemand );
        }

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            // Check the required fields
            String strRequiredField = StringUtils.EMPTY;

            if ( nIdDirectory == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_DIRECTORY;
            }
            else if ( nPositionEntryDirectoryIdDemand == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_ID_DEMAND;
            }
            else if ( StringUtils.isBlank( strStatusText ) )
            {
                strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_STATUS_TEXT;
            }
            else if ( bSendNotification )
            {
                if ( StringUtils.isBlank( strSenderName ) )
                {
                    strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_SENDER_NAME;
                }
                else if ( StringUtils.isBlank( strSubject ) )
                {
                    strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_SUBJECT;
                }
                else if ( StringUtils.isBlank( strMessage ) )
                {
                    strRequiredField = NotifyCRMConstants.PROPERTY_LABEL_MESSAGE;
                }
            }

            if ( StringUtils.isNotBlank( strRequiredField ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strRequiredField, locale ) };
                strError = AdminMessageService.getMessageUrl( request, NotifyCRMConstants.MESSAGE_MANDATORY_FIELD,
                        tabRequiredFields, AdminMessage.TYPE_STOP );
            }
        }

        return strError;
    }
}
