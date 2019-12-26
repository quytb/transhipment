update tc_lenh set created_by = 0 where created_by is null or created_by = '';
update tc_lenh set last_updated_by = 0 where last_updated_by is null or last_updated_by = '';
alter table tc_lenh modify created_by integer null;
alter table tc_lenh modify last_updated_by integer null;