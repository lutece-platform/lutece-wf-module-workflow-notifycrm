ALTER TABLE task_notify_crm_cf ADD COLUMN position_directory_entry_crm_web_app_code VARCHAR(255) DEFAULT '' NOT NULL;
ALTER TABLE task_notify_crm_cf DROP COLUMN crm_webapp_base_url;