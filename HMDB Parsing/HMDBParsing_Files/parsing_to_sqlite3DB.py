import sys
import csv
import sqlite3

# import xml.etree.ElementTree as etree
# for event, elem in etree.iterparse(xmL, events=('start', 'end', 'start-ns', 'end-ns')):
#   print event, elem


def dropAllTable():
	cursor.execute("drop table if exists HMDBTransporter ")
	cursor.execute("drop table if exists HMDBmetabolites ")
	cursor.execute("drop table if exists HMDBrelation ")




def main():

	global cursor
	metabolitesCSV = "HMDBmetabolites.csv" 
	transporterCSV = "HMDBTransporter.csv"
	relationCSV = "HMDBmetabolite_protein_link.csv"


	DBFile = "hmdb.db"
	conn = sqlite3.connect(DBFile)
	cursor = conn.cursor()

	dropAllTable()

	
	cursor.execute("create table if not exists HMDBTransporter(transporterID text, transporterName text)")
	cursor.execute("create table if not exists HMDBmetabolites(metabolitesID text, metabolitesName text, metabolitesSMILES text)")
	cursor.execute("create table if not exists HMDBrelation(metabolitesID text, transporterID text)")



	with open(transporterCSV, newline='') as csvfile:
		reader = csv.reader(csvfile)
		for row in reader:
			transporterID = row[0]
			transporterName = row[1]
			tempTuple = (transporterID,transporterName)
			cursor.execute("insert into HMDBTransporter values (?,?)",tempTuple)

	with open(metabolitesCSV, encoding='ISO-8859-1',newline='') as csvfile:
		reader = csv.reader(csvfile)
		for row in reader:

			metabolitesID = row[0]
			metabolitesName = row[1]
			metabolitesSMILES = row[2]
			tempTuple = (metabolitesID,metabolitesName,metabolitesSMILES)
			cursor.execute("insert into HMDBmetabolites values (?,?,?)",tempTuple)


	with open(relationCSV, newline='') as csvfile:
		reader = csv.reader(csvfile)
		for row in reader:
			metabolitesID = row[0]
			transporterID = row[1]

			tempTuple = (metabolitesID,transporterID)

			cursor.execute("insert into HMDBrelation values (?,?)",tempTuple)

	conn.commit()
	conn.close()


if __name__ == "__main__":
	main()