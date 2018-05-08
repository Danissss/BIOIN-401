import xml.etree.ElementTree as ET
import sys
import sqlite3








def main():

	xmlFile = sys.argv[1]
	tree = ET.parse(xmlFile)
	root = tree.getroot()
	tree = ET.parse(xmlFile)
	root = tree.getroot()
	ns = "{http://www.hmdb.ca}"

	DBFile = "HMDB.db"
	conn = sqlite3.connect(databaseFile)
	cursor = conn.cursor()
	cursor.execute("create table  HMDBTable(metabolites text, metabolites_SMILES text, metabolites_InChyKey text,\
               protein_name text, protein_type text);")




	for child in root.findall(ns+"metabolite"):
		metabolites = child.find(ns+"name").text
		metabolites_SMILES = child.find(ns+"smiles").text
		metabolites_InChyKey = child.find(ns+"inchikey").text
		protein_ass = child.find(ns+"protein_associations")
		for protein in protein_ass.findall(ns+"protein"):
			protein_name = protein.find(ns+"name").text
			protein_type = protein.find(ns+"protein_type").text

			tempTuple = (metabolites,metabolites_SMILES,metabolites_InChyKey,protein_name,protein_type)
		# for protein in protein_ass:
		# 	print(protein)
			cursor.execute("insert into HMDBTable values (?,?,?,?,?)",tempTuple)
			

			# protein_name = protein_ass.find("protein").text
			# print(protein_name)
	conn.commit()
	conn.close()


if __name__ == "__main__":
	main()
