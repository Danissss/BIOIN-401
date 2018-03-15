import csv
import sys
import itertools
# import pandas as pd

# csvFile1 will always be the result
# csvFile2 will always be the feature

def main():
	csvFile1 = sys.argv[1]
	csvFile2 = sys.argv[2]
	csvFile3 = sys.argv[3]

	file3 = open(csvFile3,"w")


	file1 = open(csvFile1,"r")
	file1_line = file1.readlines()

	file2 = open(csvFile2,"r")
	file2_line = file2.readlines()

	
	for i,j in itertools.zip_longest(file2_line, file1_line):
		tem_buffer = []

		# feature1 get all feature from file2
		feature1 = i.split(",")
		result = j.split(",")
		actions = result[len(result)-1]
		feature1_len = len(feature1)
		
		for feature in range(0,feature1_len):
			fea = feature1[feature].replace("\"","")
			tem_buffer.append(float(fea))
		tem_buffer.append(actions)

		# if combine two csv features, append more

		# for feature2 in range(0,feature2_len):
		# 	fea = feature2[feature].replace("\"","")
		# 	tem_buffer.append(float(fea))



		len_tem_buffer = len(tem_buffer)
		write_string = ""

		for i in range(0,len_tem_buffer-1):
			write_string += str(tem_buffer[i])
			write_string += ","
		write_string += str(tem_buffer[len_tem_buffer-1])

		# file_string = str(tem_buffer)
		# file_string = file_string.replace("[","")
		# file_string = file_string.replace("]","")

		file3.write(write_string)
		

		# annoyOne = feature1[feature1_len-1]
		# annoyOne = annoyOne.replace("\"","")
		# annoyOne = float(annoyOne)
		# print(annoyOne)
	

		# 	

		
		# 	tem_buffer.append(float(feature1[feature]))
		# tem_buffer.append(actions)
	
		
		sys.exit(0)

	# file1 = pd.read_csv(csvFile1)
	# file2 = pd.read_csv(csvFile2)

	# merged = file1.merge(file2)
	# merged.to_csv("combined.csv",index=False)



	# overall = []
	# with open(csvFile1) as csvFile1, open(csvFile2) as csvFile2, open(csvFile3) as outFile:
	# 	writer = csv.writer(outFile,delimiter=",")

	# 	rd1 = csv.reader(csvFile1,delimiter=",")
	# 	rd2 = csv.reader(csvFile2,delimiter=",")

	# 	for row1,row2 in rd1,rd2:
	# 		print(rd1)
	# 		print(rd2)
	# 		# writer.writerow(row1[0]+row2[1])
	# 		sys.exit(0)

	

	# filenames = ['1.csv', '2.csv']
	# handles = [open(filename, 'rb') for filename in filenames]    
	# readers = [csv.reader(f, delimiter=',') for f in handles]

	# with  open('combined.csv', 'wb') as h:
	#     writer = csv.writer(h, delimiter=',', lineterminator='\n', )
	#     for rows in IT.zip_longest(*readers, fillvalue=['']*2):
	#         combined_row = []
	#         for row in rows:
	#             row = row[:2] # select the columns you want
	#             if len(row) == 2:
	#                 combined_row.extend(row)
	#             else:
	#                 combined.extend(['']*2)
	#         writer.writerow(combined_row)

	# for f in handles:
	#     f.close()
	


if __name__ == "__main__":
	main()

	# import csv

	# with open('path/to/file1') as infile1, open('path/to/file2') as infile2, open('path/to/output', 'w') as outfile:
	#     writer = csv.writer(outfile, delimiter='\t')
	#     for row1,row2 in zip(csv.reader(infile1, delimiter='\t'), csv.reader(infile2, delimiter='\t')):
	#         writer.writerow(row1+row2)