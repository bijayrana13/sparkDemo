Please refer below details to have an overview about code as well as point 3 depicts instructions on how to execute the code.

NOTE: For detail understanding , go through the code also the comments mentioned in code.
    Scala Code location: src.main.scala.retail.db.RevenuePerProduct.scala

1) Below point mentions about any optimisation used in code and also other details:
   --------------------------------------------------------------------------------
 Other outputs will be saved into output location provided as 3rd argument value while running the job.
 I have used coalesce to avoid shuffling of data and writing into a single output file. 
 I have used mergeSchema to true in option while savind dataframe into t output location, this will help in dynamic schema handling, If any extra columns comes from source that will be added to target and existing record will have null values for the same column.
	  

2)Below compatible software Versions used via build.sbt file:
  -----------------------------------------------------------
Spark 3.2.3
Scala 2.12.15
jdk : jdk 8u 202
sbt: 1.8.2


3) Follow below Instruction to run the program:
   --------------------------------------------
a)Go to project directory
cd sparkDemo
 
b)Build executable jars 
sbt package

c)Execute Via Spark submit:

spark-submit --class retail.db.RevenuePerProduct --jars /mnt/c/jar/spark-excel_2.12-3.3.1_0.18.5.jar /mnt/c/Users/bijay/IdeaProjects/sparkDemo/target/scala-2.12/sparkdemo_2.12-0.1.0-SNAPSHOT.jar local[*] /mnt/c/data/online_retail_II.xlsx /mnt/c/data/stock_revenue/

	  Job takes 3 argumens
	      i)Master while creating spark session Object (has to be local or yarn etc..)
          ii)Input file path
	      iii)Output file path
		  
d) Go to stock_revenue output folder and check respective sub folder for output data validation.

e) To check Top 10 popular product based on total quantity sold, check job log file in console output as i am not saving it into any location.
