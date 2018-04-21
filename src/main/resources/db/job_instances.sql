create schema if not exists journal;

drop table if exists public.job_instances;
drop table if exists journal.job_instances cascade;
delete from partman.part_config where parent_table = 'journal.job_instances';

set search_path to public;

create table job_instances (
  id                          text primary key check(util.non_empty_trimmed_string(id)),
  key                         text not null check(util.non_empty_trimmed_string(key)),
  job                         json not null,
  input                       json,
  output                      json,
  errors                      json,
  created_at                  timestamptz not null default now(),
  updated_at                  timestamptz not null default now(),
  updated_by_user_id          text not null check(util.non_empty_trimmed_string(updated_by_user_id)),
  hash_code                   bigint not null
);

create index job_instances_job_idx on job_instances(job);

select schema_evolution_manager.create_updated_at_trigger('public', 'job_instances');

select journal.refresh_journaling('public', 'job_instances', 'journal', 'job_instances');
select partman.create_parent('journal.job_instances', 'journal_timestamp', 'time', 'monthly');

update partman.part_config
set retention = '6 month',
  retention_keep_table = false,
  retention_keep_index = false
where parent_table = 'journal.job_instances';