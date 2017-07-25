create table bmt_qry_rsa (
seq bigint primary key,
id varchar(15),
jumin_num varchar(13),
country varchar(20),
state varchar(20),
location varchar(20),
organization varchar(20),
orginization_unit varchar(20),
company_name varchar(20),
subject_name varchar(20),
pub_pem varchar(2000),
pri_pem varchar(2000),
hash_msg varchar(70),
sign_msg varchar(140),
sign_msg_login varchar(300)
);

create index bmt_qry_rsa_id_index on bmt_qry_rsa(id);