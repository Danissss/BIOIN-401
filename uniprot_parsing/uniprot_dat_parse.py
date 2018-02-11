 


# database stuff
import sqlite3
conn = sqlite3.connect("uniprotDB.db")
c = conn.cursor()
c.execute("drop table if exists uniprot_database;")
c.execute("create table uniprot_database (uniprot_ID char(20), full_name char(20), ID char(20),gene_ID char(20),\
            organism char(20), transport boolean);")

fileName = "uniprot_sprot.dat"
f = open(fileName,"r")
files = f.readlines()
files_length = len(files)

start_index = 0
end_index = 0
block=[]
for index in range(start_index,files_length):
    i = files[index]
    if i == "//" or i =='//\n':
        end_index = index
        for index in range(start_index, end_index-1):
            block.append(files[index])
        # print(len(block))
        
        
        # work on single block:
        ID = None
        GeneID = None
        Organism = None
        transport = 0


        determine = []
        for real_i in block:

            #determine transport or not
            #easily get all the protein that is transporters
            if "DR   GO;" in real_i:
                determine.append(real_i)
            for is_transport in determine:
                if "transporter" in is_transport:
                    transport = 1
            determine.clear()
            
            
            
            # get uniprot_ID
            if "OS   " in real_i:
                Organism_split = real_i.split("   ")
                Organism = Organism_split[1].replace(".","")

            # get ID of uniprot
            if "ID   " in real_i:
                ID_split = real_i.split(" ")
                ID = ID_split[3]
            # get GeneID
            if "DR   GeneID" in real_i:
                GeneID_split = real_i.split("; ")
                GeneID = GeneID_split[1]
                GeneID = int(GeneID)
            # get full name
            if "DE   RecName" in real_i:
                FullName_split = real_i.split("=")
                FullName = FullName_split[1].replace(";","")

            if "AC   " in real_i:
                UniprotID_split = real_i.split(";")
                UniprotID = UniprotID_split[0].replace("AC   ","")
        # print(GeneID, ID, Organism,FullName,UniprotID,transport )   
        temp_tuple = (UniprotID,FullName,ID,GeneID,Organism,transport)
        c.execute("insert into uniprot_database values(?,?,?,?,?,?)",temp_tuple)   

        
        #reset the block
        start_index = end_index+1
        block.clear()

        

conn.commit()
conn.close()

f.close()