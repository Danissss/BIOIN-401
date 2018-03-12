import xml.etree.ElementTree as ET
import sqlite3
import sys

# "get" will only get attribute name's text
# "find" will find the text inside the tag that "find" is look for


xmlFile = "testData_drugbank.xml"
storeFile = "store_file.txt"
tree = ET.parse(xmlFile)
root = tree.getroot()
ns = '{http://www.drugbank.ca}'





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
    # print(temp_tuple)


    trasnport_id = None
    transport_name = None

    for transport in child.findall(ns+"transporters"):
        #transportNum = transport.get("position").text
        
        for i in transport.findall(ns+"transporter"):
            transport_id = i.find(ns+"id").text
            transport_name = i.find(ns+"name").text
            
            transport_actions = ""
            for j in i.findall(ns+"actions"):
                for k in j.findall(ns+"action"):
                    transport_actions += "|"+str(k.text)
            transport_actions += "|"
   
                # transport_actions += 
            # print(transport_actions)
                
            #print(drug_ID,transport_id,transport_name)
            temp_tuple = (drug_ID, transport_id,transport_name,transport_actions)
                
            

            print(temp_tuple)
    





