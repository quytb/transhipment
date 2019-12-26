update tc_lich_truc set created_by = 0 where created_by is null or created_by = '';
update tc_lich_truc set last_updated_by = 0 where last_updated_by is null or last_updated_by = '';
alter table tc_lich_truc modify created_by integer null;
alter table tc_lich_truc modify last_updated_by integer null;