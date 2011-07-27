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
    public static final String PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE = "workflow-notifycrm.acceptedDirectoryEntryTypes";
    public static final String PROPERTY_REFUSED_DIRECTORY_ENTRY_TYPE = "workflow-notifycrm.refusedDirectoryEntryTypes";
    public static final String PROPERTY_DEFAULT_SENDER_NAME = "workflow-notifycrm.defaultSenderName";
    public static final String PROPERTY_LABEL_REFERENCED_ENTRY = "module.workflow.notifycrm.task_notify_crm_config.label_reference_entry";
    public static final String PROPERTY_LABEL_DIRECTORY = "module.workflow.notifycrm.task_notify_crm_config.label_task_directory";
    public static final String PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_ID_DEMAND = "module.workflow.notifycrm.task_notify_crm_config.label_task_entry_directory_id_demand";
    public static final String PROPERTY_LABEL_SENDER_NAME = "module.workflow.notifycrm.task_notify_crm_config.label_sender_name";
    public static final String PROPERTY_LABEL_SUBJECT = "module.workflow.notifycrm.task_notify_crm_config.label_subject";
    public static final String PROPERTY_LABEL_MESSAGE = "module.workflow.notifycrm.task_notify_crm_config.label_message";
    public static final String PROPERTY_LABEL_STATUS_TEXT = "module.workflow.notifycrm.task_notify_crm_config.label_status_text";
    public static final String PROPERTY_WEBSERVICE_CRM_NOTIFICATION_REST_URL = "workflow-notifycrm.webapp.crmNotification.rest.url";

    // MARKS
    public static final String MARK_CONFIG = "config";
    public static final String MARK_LIST_STATES = "list_states";
    public static final String MARK_LIST_ENTRIES = "list_entries";
    public static final String MARK_LIST_DIRECTORIES = "list_directories";
    public static final String MARK_LIST_ENTRIES_FREEMARKER = "list_entries_freemarker";
    public static final String MARK_DEFAULT_SENDER_NAME = "default_sender_name";
    public static final String MARK_MESSAGE = "message";
    public static final String MARK_STATUS = "status";
    public static final String MARK_POSITION = "position_";
    public static final String MARK_DIRECTORY_TITLE = "directory_title";
    public static final String MARK_DIRECTORY_DESCRIPTION = "directory_description";

    // PARAMETERS
    public static final String PARAMETER_SUBJECT = "subject";
    public static final String PARAMETER_MESSAGE = "message";
    public static final String PARAMETER_SENDER_NAME = "sender_name";
    public static final String PARAMETER_ID_DIRECTORY = "id_directory";
    public static final String PARAMETER_POSITION_ENTRY_DIRECTORY_ID_DEMAND = "position_entry_directory_id_demand";
    public static final String PARAMETER_APPLY = "apply";
    public static final String PARAMETER_NOTIFICATION_OBJECT = "notification_object";
    public static final String PARAMETER_NOTIFICATION_MESSAGE = "notification_message";
    public static final String PARAMETER_NOTIFICATION_SENDER = "notification_sender";
    public static final String PARAMETER_ID_DEMAND = "id_demand";
    public static final String PARAMETER_STATUS_TEXT = "status_text";
    public static final String PARAMETER_DEMAND_DATA = "demand_data";
    public static final String PARAMETER_ID_STATUS_CRM = "id_status_crm";

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
