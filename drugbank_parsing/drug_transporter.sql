
-- this query will output the combination of drug, smiles string, and actions for its transporter (this case is BE0001032)
with drug(drug_id, drug_state, drug_smiles) as (select drug_id,drug_state, drug_smiles from drugbank_drug where drug_smiles is not NULL),
transporter(drug_id,actions) as (select drug_id, actions from drugbank_transport where drug_transport_id = 'BE0001032'),
data(drug_id,drug_state,drug_smiles,actions) as (select drug.drug_id, drug.drug_state,drug.drug_smiles, trans.actions from 
drug left outer join transporter trans where trans.drug_id = drug.drug_id)


select * from data;