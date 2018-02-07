import xml.etree.ElementTree as ET
import sqlite3

# "get" will only get attribute name's text
# "find" will find the text inside the tag that "find" is look for


xmlFile = "full database.xml"
storeFile = "store_file.txt"
tree = ET.parse(xmlFile)
root = tree.getroot()
ns = '{http://www.drugbank.ca}'

databaseFile = "drugbank.db"
conn = sqlite3.connect(databaseFile)
cursor = conn.cursor()

cursor.execute("drop table if exists drugbank_drug;")
cursor.execute("drop table if exists drugbank_transport;")
cursor.execute("drop view  if exists combine_;")
cursor.execute("create table drugbank_drug (drug_id char(20), drug_type char(20), drug_name char(20),\
               drug_state char(20), drug_group char(20), drug_smiles char(20), drug_InChIKey char(30), primary key (drug_id));")
cursor.execute("create table drugbank_transport (drug_id char(20), drug_transport_id char(20), drug_transport_name char(20),\
               foreign key (drug_id) references drugbank_drug(drug_id));")




# for child in root:
#     print(child.tag, child.attrib)
# output: {http://www.drugbank.ca}drug {'type': 'small molecule', 'updated': '2017-12-20', 'created': '2017-12-18'}
# for child in root.findall(ns+"drug"):
#     drug_type = child.get("type")
#     drug_ID = child.find(ns+"drugbank-id").text
#     print(drug_type,drug_ID)
# output: small molecule DB09229 (success)

for child in root.findall(ns+"drug"):
    
    drug_type = child.get("type")
    drug_ID = child.find(ns+"drugbank-id").text
    drug_name = child.find(ns+"name").text
    
    drug_state = None
    if child.find(ns+"state") is not None:
        drug_state = child.find(ns+"state").text
    
    #get drug group
    drug_group2 = None
    if child.findall(ns+"groups") is not None:
        drug_group = child.findall(ns+"groups")
        for i in drug_group:
            if i.find(ns+"group").text == "approved":
                drug_group2 = i.find(ns+"group").text
            else:
                drug_group2 = "unapproved"
    else:
        drug_group2 = None

    #get the properties of smiles and InChIkey
    drug_smiles = None
    drug_InChIKey = None
    if child.findall(ns+"calculated-properties") is not None:
        
        drug_calculated_properties = child.findall(ns+"calculated-properties")
        for properties in drug_calculated_properties:
            drug_properties = properties.findall(ns+"property")
            for single_property in drug_properties:
                if single_property.find(ns+"kind") is not None and single_property.find(ns+"kind").text == "SMILES":
                    drug_smiles = single_property.find(ns+"value").text
                #print(drug_smiles)
                if single_property.find(ns+"kind") is not None and single_property.find(ns+"kind").text == "InChIKey":
                    drug_InChIKey = single_property.find(ns+"value").text
    else:
        drug_smiles = None
        drug_InChIKey = None

    #print(drug_ID, drug_type, drug_name, drug_state,drug_group2,drug_smiles, drug_InChIKey)
    temp_tuple = (drug_ID,drug_type,drug_name,drug_state,drug_group2,drug_smiles,drug_InChIKey)
    cursor.execute("insert into drugbank_drug values (?,?,?,?,?,?,?)",temp_tuple)
    for transport in child.findall(ns+"transporters"):
        #transportNum = transport.get("position").text
        
        for i in transport.findall(ns+"transporter"):
            transport_id = i.find(ns+"id").text
            transport_name = i.find(ns+"name").text
            #print(drug_ID,transport_id,transport_name)
            temp_tuple = (drug_ID, transport_id,transport_name)
                
            cursor.execute("insert into drugbank_transport values (?,?,?)",temp_tuple)
# if transport.find(ns+"id") is None or transport.find(ns+"name") is None:
#     transport_id = "NULL"
#     transport_name = "NULL"
#     print(drug_ID,transport_id,transport_name)
# else:
#     for i in transport.findall(ns+"transporter"):
#         transport_id = i.find(ns+"id").text
#         transport_name = i.find(ns+"name").text
#         print(drug_ID,transport_id,transport_name)
#cursor.execute("insert into drugbank_transport values (" + drug_ID + "," + transport_id + "," + trasnport_name + ");")

# get combined dataset
cursor.execute("create view combine_  as select drug.drug_id, drug.drug_type,drug.drug_name,drug.drug_state,drug.drug_group,transport.drug_transport_id,transport.drug_transport_name from drugbank_drug drug left outer join drugbank_transport transport on drug.drug_id = transport.drug_id;")

conn.commit()
conn.close()
