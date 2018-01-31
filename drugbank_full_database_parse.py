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

cursor.execute("drop table drugbank_drug;")
cursor.execute("drop table drugbank_transport;")
cursor.execute("create table drugbank_drug (drug_id char(20), drug_type char(20), drug_name char(20),\
               primary key (drug_id));")   # drug_state char(20)
cursor.execute("create table drugbank_transport (drug_id char(20), drug_transport_id char(20), drug_transport_name char(20),\
               foreign key (drug_id) references drugbank_drug(drug_id));")



for child in root.findall(ns+"drug"):
    
    drug_type = child.get("type")
    drug_ID = child.find(ns+"drugbank-id").text
    drug_name = child.find(ns+"name").text
    #drug_state = child.find(ns+"state").text
    drug_group = child.findall(ns+"groups")
    for i in drug_group:
        drug_group2 = i.find(ns+"group").text
    #print(drug_ID, drug_name, drug_state, drug_group2)
    temp_tuple = (drug_ID,drug_type,drug_name)   #,drug_state
    cursor.execute("insert into drugbank_drug values (?,?,?)",temp_tuple)
    for transport in child.findall(ns+"transporters"):
        #transportNum = transport.get("position").text
        
        for i in transport.findall(ns+"transporter"):
            transport_id = i.find(ns+"id").text
            transport_name = i.find(ns+"name").text
            temp_tuple_transporter = (drug_ID,transport_id,transport_name)
            cursor.execute("insert into drugbank_transport values (?,?,?)",temp_tuple_transporter)


conn.commit()
conn.close()
