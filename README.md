# BIOIN-401
This is Bioinformatics 401 project repo

#Open source:<br/>
Weka 3: Data Mining Software in Java (machine learning station)<br/>
CDK (chemistry development kit) GNU Lesser General Public License, version 2.1 (or later).<br/>

#other resources can be found in wiki page


# create dataset for weka from table in sqlite database
//create dataset2: question: transportable or untransportable?; method: associated drug against non-associated drug
create table dataset2 as select drug.drug_id as "drug_id", drug.drug_state as "drugState",drug.drug_smiles as "drugSmiles",transport.drug_transport_id as "transportID" from drugbank_drug drug left outer join drugbank_transport transport on drug.drug_id = transport.drug_id and drug.drug_type = 'small molecule' and drug.drug_smiles is not Null;<br/>
delete from dataset2 where drugSmiles is Null;<br/>
update dataset2 set transportID = 1 where transportID is not Null;<br/>
