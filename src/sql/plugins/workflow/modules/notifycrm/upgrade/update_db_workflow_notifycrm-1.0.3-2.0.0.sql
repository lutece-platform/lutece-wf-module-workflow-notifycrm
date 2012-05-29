--
-- WORKFLOWNOTIFYCRM-7 : Add the possibility to notify a specific CRM webapp
--
ALTER TABLE task_notify_crm_cf ADD COLUMN crm_webapp_base_url VARCHAR(255) DEFAULT '' NOT NULL;
