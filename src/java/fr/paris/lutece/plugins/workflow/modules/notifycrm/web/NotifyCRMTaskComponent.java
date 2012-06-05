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
package fr.paris.lutece.plugins.workflow.modules.notifycrm.web;

import fr.paris.lutece.plugins.workflow.modules.notifycrm.business.TaskNotifyCRMConfig;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.service.INotifyCRMService;
import fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants.NotifyCRMConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflow.service.security.IWorkflowUserAttributesManager;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;


/**
 *
 * NotifyCRMTaskComponent
 *
 */
public class NotifyCRMTaskComponent extends NoFormTaskComponent
{
    private static final String TEMPLATE_TASK_NOTIFY_CRM_CONFIG = "admin/plugins/workflow/modules/notifycrm/task_notify_crm_config.html";
    @Inject
    @Named( NotifyCRMConstants.BEAN_TASK_CONFIG_SERVICE )
    private ITaskConfigService _taskNotifyCRMConfigService;
    @Inject
    private INotifyCRMService _notifyCRMService;
    @Inject
    private IWorkflowUserAttributesManager _userAttributesManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        // In case there are no errors, then the config is created/updated
        boolean bCreate = false;
        TaskNotifyCRMConfig config = _taskNotifyCRMConfigService.findByPrimaryKey( task.getId(  ) );

        if ( config == null )
        {
            config = new TaskNotifyCRMConfig(  );
            config.setIdTask( task.getId(  ) );
            bCreate = true;
        }

        try
        {
            BeanUtils.populate( config, request.getParameterMap(  ) );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "NotifyCRMTaskComponent - Unable to fetch data from request", e );
        }
        catch ( InvocationTargetException e )
        {
            AppLogService.error( "NotifyCRMTaskComponent - Unable to fetch data from request", e );
        }

        String strApply = request.getParameter( NotifyCRMConstants.PARAMETER_APPLY );

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            // Check mandatory fields
            Set<ConstraintViolation<TaskNotifyCRMConfig>> constraintViolations = BeanValidationUtil.validate( config );

            if ( constraintViolations.size(  ) > 0 )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
        }

        if ( bCreate )
        {
            _taskNotifyCRMConfigService.create( config );
        }
        else
        {
            _taskNotifyCRMConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        String strDefaultSenderName = AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_DEFAULT_SENDER_NAME );
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( NotifyCRMConstants.MARK_CONFIG, _taskNotifyCRMConfigService.findByPrimaryKey( task.getId(  ) ) );
        model.put( NotifyCRMConstants.MARK_DEFAULT_SENDER_NAME, strDefaultSenderName );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_ID_DEMAND,
            _notifyCRMService.getListEntriesIdDemand( task.getId(  ), locale ) );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_USER_GUID,
            _notifyCRMService.getListEntriesUserGuid( task.getId(  ), locale ) );
        model.put( NotifyCRMConstants.MARK_LIST_DIRECTORIES, _notifyCRMService.getListDirectories(  ) );
        model.put( NotifyCRMConstants.MARK_LIST_ENTRIES_FREEMARKER,
            _notifyCRMService.getListEntriesFreemarker( task.getId(  ) ) );
        model.put( NotifyCRMConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( NotifyCRMConstants.MARK_LOCALE, request.getLocale(  ) );
        model.put( NotifyCRMConstants.MARK_IS_USER_ATTRIBUTE_WS_ACTIVE, _userAttributesManager.isEnabled(  ) );
        model.put( NotifyCRMConstants.MARK_PLUGIN_WORKFLOW, pluginWorkflow );
        model.put( NotifyCRMConstants.MARK_LOCALE, locale );
        model.put( NotifyCRMConstants.MARK_TASKS_LIST,
            _notifyCRMService.getListTasks( task.getAction(  ).getId(  ), locale ) );
        model.put( NotifyCRMConstants.MARK_DEFAULT_CRM_WEBAPP_BASE_URL,
            AppPropertiesService.getProperty( NotifyCRMConstants.PROPERTY_CRMCLIENT_REST_WEBAPP_URL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_CRM_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }
}
