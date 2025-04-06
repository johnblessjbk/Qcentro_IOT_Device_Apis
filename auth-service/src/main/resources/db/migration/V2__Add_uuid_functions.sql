
CREATE FUNCTION uuid_to_bin(uuid CHAR(36)) 
RETURNS BINARY(16) DETERMINISTIC
RETURN UNHEX(REPLACE(uuid, '-', ''));

CREATE FUNCTION bin_to_uuid(bin BINARY(16)) 
RETURNS CHAR(36) DETERMINISTIC
RETURN LOWER(CONCAT_WS('-',
    HEX(SUBSTRING(bin, 1, 4)),
    HEX(SUBSTRING(bin, 5, 2)),
    HEX(SUBSTRING(bin, 7, 2)),
    HEX(SUBSTRING(bin, 9, 2)),
    HEX(SUBSTRING(bin, 11, 6))
));