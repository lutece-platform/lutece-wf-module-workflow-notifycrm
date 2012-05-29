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
package fr.paris.lutece.plugins.workflow.modules.notifycrm.util.constants;


/**
 *
 * NotifyCRMConstants
 *
 */
public final class NotifyCRMConstants
{
    // CONSTANTS
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSED_BRACKET = ")";
    public static final String HYPHEN = "-";
    public static final String ID = "id";
    public static final String NAME = "name";

    // PROPERTIES
    public static final String PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE_ID_DEMAND = "workflow-notifycrm.acceptedDirectoryEntryTypesIdDemand";
    public static final String PROPERTY_REFUSED_DIRECTORY_ENTRY_TYPE = "workflow-notifycrm.refusedDirectoryEntryTypes";
    public static final String PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE_USER_GUID = "workflow-notifycrm.acceptedDirectoryEntryTypesUserGuid";
    public static final String PROPERTY_DEFAULT_SENDER_NAME = "workflow-notifycrm.defaultSenderName";
    public static final String PROPERTY_LABEL_REFERENCED_ENTRY = "module.workflow.notifycrm.task_notify_crm_config.label_reference_entry";
    public static final String PROPERTY_LABEL_DIRECTORY = "module.workflow.notifycrm.task_notify_crm_config.label_task_directory";
    public static final String PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_ID_DEMAND = "module.workflow.notifycrm.task_notify_crm_config.label_task_entry_directory_id_demand";
    public static final String PROPERTY_LABEL_SENDER_NAME = "module.workflow.notifycrm.task_notify_crm_config.label_sender_name";
    public static final String PROPERTY_LABEL_SUBJECT = "module.workflow.notifycrm.task_notify_crm_config.label_subject";
    public static final String PROPERTY_LABEL_MESSAGE = "module.workflow.notifycrm.task_notify_crm_config.label_message";
    public static final String PROPERTY_LABEL_STATUS_TEXT = "module.workflow.notifycrm.task_notify_crm_config.label_status_text";
    public static final String PROPERTY_WEBSERVICE_REST_CRM_NOTIFICATION_WEBAPP_URL = "workflow-notifycrm.rest.crmNotification.webapp.url";
    public static final String PROPERTY_CRMCLIENT_REST_WEBAPP_URL = "crmclient.crm.rest.webapp.url";

    // MARKS
    public static final String MARK_CONFIG = "config";
    public static final String MARK_LIST_ENTRIES_ID_DEMAND = "list_entries_id_demand";
    public static final String MARK_LIST_ENTRIES_USER_GUID = "list_entries_user_guid";
    public static final String MARK_LIST_DIRECTORIES = "list_directories";
    public static final String MARK_LIST_ENTRIES_FREEMARKER = "list_entries_freemarker";
    public static final String MARK_DEFAULT_SENDER_NAME = "defaultSenderName";
    public static final String MARK_MESSAGE = "message";
    public static final String MARK_STATUS = "status";
    public static final String MARK_POSITION = "position_";
    public static final String MARK_DIRECTORY_TITLE = "directory_title";
    public static final String MARK_DIRECTORY_DESCRIPTION = "directory_description";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_IS_USER_ATTRIBUTE_WS_ACTIVE = "is_user_attribute_ws_active";
    public static final String MARK_TASK = "task_";
    public static final String MARK_PLUGIN_WORKFLOW = "plugin_workflow";
    public static final String MARK_TASKS_LIST = "tasks_list";
    public static final String MARK_FIRST_NAME = "first_name";
    public static final String MARK_LAST_NAME = "last_name";
    public static final String MARK_EMAIL = "email";
    public static final String MARK_PHONE_NUMBER = "phone_number";
    public static final String MARK_DEFAULT_CRM_WEBAPP_BASE_URL = "defaultCRMWebAppBaseURL";

    // PARAMETERS
    public static final String PARAMETER_SUBJECT = "subject";
    public static final String PARAMETER_MESSAGE = "message";
    public static final String PARAMETER_SENDER_NAME = "sender_name";
    public static final String PARAMETER_ID_DIRECTORY = "id_directory";
    public static final String PARAMETER_POSITION_ENTRY_DIRECTORY_ID_DEMAND = "position_entry_directory_id_demand";
    public static final String PARAMETER_POSITION_ENTRY_DIRECTORY_USER_GUID = "position_entry_directory_user_guid";
    public static final String PARAMETER_SEND_NOTIFICATION = "send_notification";
    public static final String PARAMETER_APPLY = "apply";
    public static final String PARAMETER_NOTIFICATION_OBJECT = "notification_object";
    public static final String PARAMETER_NOTIFICATION_MESSAGE = "notification_message";
    public static final String PARAMETER_NOTIFICATION_SENDER = "notification_sender";
    public static final String PARAMETER_ID_DEMAND = "id_demand";
    public static final String PARAMETER_STATUS_TEXT = "status_text";
    public static final String PARAMETER_DEMAND_DATA = "demand_data";
    public static final String PARAMETER_ID_STATUS_CRM = "id_status_crm";
    public static final String PARAMETER_CRM_WEBAPP_BASE_URL = "crm_web_app_base_url";

    // MESSAGES
    public static final String MESSAGE_MANDATORY_FIELD = "module.workflow.notifycrm.message.mandatoryField";

    // URL
    public static final String URL_REST_NOTIFY = "/rest/crm/notify/demand";
    public static final String URL_REST_UPDATE_DEMAND = "/rest/crm/demand/update";

    /**
     * Private constructor
     */
    private NotifyCRMConstants(  )
    {
    }
}
