 


fileName = "test_uniprot_data.dat"
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
        transport = False


        determine = []
        for real_i in block:

            #determine transport or not
            #easily get all the protein that is transporters
            if "DR   GO;" in real_i:
                determine.append(real_i)
            for is_transport in determine:
                if "transport" in is_transport:
                    transport = True
            print(determine)
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
        print(GeneID, ID, Organism,FullName,UniprotID,transport )        

        
        #reset the block
        start_index = end_index+1
        block.clear()

        



f.close()