-- Add long/lat for tc_ve--
ALTER TABLE tc_ve
ADD COLUMN bvv_long_start DOUBLE,
ADD COLUMN bvv_lat_start DOUBLE,
ADD COLUMN bvv_long_end DOUBLE,
ADD COLUMN bvv_lat_end DOUBLE;
-- Add source tc_ve--
ALTER TABLE tc_ve
ADD COLUMN bvv_source int