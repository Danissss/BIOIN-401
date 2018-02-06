# This file parse json file from KEGG database
# version 1.0 (final version)
# author: Xuan Cao "github: Danissss"





import json
import sqlite3

data1 = json.load(open('br08310.json'))
sqlite_file = "KEGG_br08310_drug_database.db"
conn = sqlite3.connect(sqlite_file)
cursor = conn.cursor()
cursor.execute("drop table if exists br08310_drug_data;")
cursor.execute("drop table if exists protein_table;")
cursor.execute("create table br08310_drug_data (a char(50), b char(50), c char(50), \
            d char(50), e char(50), ID char(20), name char(20));")
cursor.execute("create table protein_table (ID char(20), name char(20), type char(20), foreign key (ID) references \
                br08310_drug_data(ID)); ")
# data1_child = data1["name"]   # = str "ko00000"

data1_child = data1["children"]  # data1_child is a list with 7 element


#The continue statement, also borrowed from C, continues with the next iteration of the loop:


for a in range(len(data1_child)):
    a_child = data1_child[a]["name"]

    if data1_child[a].get("children"):
        data1_child2 = data1_child[a]["children"]
    else:
        temp_tuple = (a_child,None,None,None,None,None,None)
        cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)

        continue

        
        

    for b in range(len(data1_child2)):
        b_child = data1_child2[b]["name"]

        if data1_child2[b].get("children"):
            data1_child3 = data1_child2[b]["children"]
        else:
            temp_tuple = (a_child,b_child,None,None,None,None,None)
            cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)

            continue
            
            

        for c in range(len(data1_child3)):
            c_child = data1_child3[c]["name"]

            if data1_child3[c].get("children"):
                data1_child4 = data1_child3[c]["children"]
            else:
                temp_tuple = (a_child,b_child,c_child,None,None,None,None)
                cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)

                continue
                

            for d in range(len(data1_child4)):
                d_child = data1_child4[d]["name"]

                if data1_child4[d].get("children"):
                    data1_child5 = data1_child4[d]["children"]
                else:
                    temp_tuple = (a_child,b_child,c_child,d_child,None,None,None)
                    cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)

                    continue
                   
                    
                for e in range(len(data1_child5)):
                    e_child = data1_child5[e]["name"]

                    if data1_child5[e].get("children"):
                        data1_child6 = data1_child5[e]["children"]
                    else:
                        
                        temp_tuple = (a_child,b_child,c_child,d_child,e_child,None,None)
                        cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)
                    
                        continue
                        
                    for f in range(len(data1_child6)):

                      
                        f_child = data1_child6[f]["name"]
                        f_child_split = f_child.split(" ")
                        ID = f_child_split[0]

                        length = len(f_child_split)
                        types = f_child_split[length-1]
                        types_split = types.split("\t")
                        length2 = len(types_split)
                        protein_type = types_split[length2-1]
                        protein_name = ""
                        for l in range(1,length-1):
                            protein_name = protein_name + " " + f_child_split[l]

                        # print(ID, name)
                        # types_not_split =  f_child_split[3].split("\t")
                        # print(types_not_split[1])
                       


                        temp_tuple_protein_table= (ID,protein_name,protein_type)
                        cursor.execute("insert into protein_table values (?,?,?)",temp_tuple_protein_table)
                        temp_tuple = (a_child,b_child,c_child,d_child,e_child,ID,protein_name)
                        cursor.execute("insert into br08310_drug_data values (?,?,?,?,?,?,?)",temp_tuple)
                       
                        
conn.commit()
conn.close()
print("database build finished")



                        














# data1_child2 = data1_child[0]["children"]  # data1_child is a list with 12 element
# # print(len(data1_child_Metabolism))    #12
# # print(type(data1_child_Metabolism))   #list

# # for i in range(len(data1_child_child)):
# #     print(data1_child_child[i]["name"])



# data1_child3 = data1_child2[0]["children"]  # data1_child is a list with 15 element

# # for i in range(len(data1_child3)):
# #     print(data1_child3[i]["name"])

# data1_child4 = data1_child3[0]["children"]
# # for i in range(len(data1_child4)):
# #     print(data1_child4[i]["name"])

# data1_child5 = data1_child4[0]["children"]

# # for i in range(len(data1_child5)):
# #     print(data1_child5[i]["name"])

# data1_child6 = data1_child5[0]["children"]
# # for i in range(len(data1_child6)):
# #     print(data1_child6[i]["name"])