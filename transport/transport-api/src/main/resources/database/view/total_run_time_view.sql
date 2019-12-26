CREATE OR REPLACE VIEW total_run_time_view AS
select rod.rod_tuyen_id AS tuyen_id, rod.rod_dich_vu_id as dich_vu_id, rod.rod_routing_option_id as ro_id, sum(rod
    .rod_time_run) as
    run_time
from routing_option_detail rod
group by rod.rod_tuyen_id, rod.rod_dich_vu_id, rod.rod_routing_option_id