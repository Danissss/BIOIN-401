
-- this query will output the combination of drug, smiles string, and actions for its transporter (this case is BE0001032)
with drug(drug_id, drug_state, drug_smiles) as (select drug_id,drug_state, drug_smiles from drugbank_drug where drug_smiles is not NULL),
transporter(drug_id,actions) as (select drug_id, actions from drugbank_transport where drug_transport_id = 'BE0001032'),
data(drug_id,drug_state,drug_smiles,actions) as (select drug.drug_id, drug.drug_state,drug.drug_smiles, trans.actions from 
drug left outer join transporter trans where trans.drug_id = drug.drug_id)


select * from data;



#select for all drug and MDR1
with combined(drug_name, drug_smile, transport_id) as (select drug.drug_name,drug.drug_smiles,transport.drug_transport_id from drugbank_drug drug left outer join drugbank_transport transport on drug.drug_id = transport.drug_id),
combined2(drug_name, drug_smile, transport_id) as (select drug_name, drug_smile, transport_id from combined
where drug_smile is not null and transport_id is null or transport_id = 'BE0001032')

select * from combined2;



# create tale for MDR1
drop table MDR1;
create table MDR1(drug_name text, drug_smile text , transport_id text);


with combined(drug_name, drug_smile, transport_id) as (select drug.drug_name,drug.drug_smiles,transport.drug_transport_id from drugbank_drug drug left outer join drugbank_transport transport on drug.drug_id = transport.drug_id),
combined2(drug_name, drug_smile, transport_id) as (select drug_name, drug_smile, transport_id from combined
where drug_smile is not null)

insert into MDR1 select * from combined2;

update MDR1 set transport_id = 0 where transport_id is null;
update MDR1 set transport_id = 0 where transport_id != 'BE0001032';
update MDR1 set transport_id = 1 where transport_id = 'BE0001032';

#check correctness
select count(*) from MDR1 where transport_id = 1;

# get the csv
select distinct drug_name, drug_smile, transport_id from ABCG2;