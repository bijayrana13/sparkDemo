package retail.db

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object RevenuePerProduct {

    def main(args: Array[String]): Unit = {

      val spark = SparkSession.builder()
        .master(args(0))
        .appName("SparkByExample")
        .getOrCreate();


      val outputlocation = args(2)

      //1.Dataset loaded using spark excel library
      val orderDf = spark.read
        .format("com.crealytics.spark.excel")
        //.option("sheetName", "Year 2009-2010")
        .option("Header", "true")
        .option("treatEmptyValuesAsNulls", "true")
        .option("inferSchema", "true")
        .option("addColorColumns", "False")
        //.load("src/main/resources/online_retail_II_1.xlsx")
        .load(args(1))



      //2. Removing missing & invalid values
      //Missing Values: Customer id , stockCode and Invoice can not be null as there has to be valid customer purchasing a valid product(stockcode) and hence a valid invoice should be generated
      //Invalid Values: Quantity purchased can not be a negative number
      val validDataDf = orderDf.filter( orderDf("Customer ID").isNotNull and orderDf("StockCode").isNotNull and orderDf("Invoice").isNotNull).filter(orderDf("Quantity") >= 0)

      val revenueDf = validDataDf.withColumn("revenue", col("Quantity") * col("Price"))

      revenueDf.coalesce(1).write.option("mergeSchema", "true").csv(s"${outputlocation}/revenue")

      //3.Calculating Total revenue per product
      val RevenuePerProductDf = revenueDf.groupBy("StockCode").agg(sum("revenue").as("revenuePerProduct"))
      RevenuePerProductDf.coalesce(1).write.option("mergeSchema", "true").csv(s"${outputlocation}/revenuePerproduct")


      //4.Fetching top 10 popular product based on total quantity sold
      val totalQuantityPerProductDf = validDataDf.groupBy("StockCode").agg(sum("Quantity").as("totalQuantity")).orderBy(desc("totalQuantity"))

      totalQuantityPerProductDf.show(10)


      //5.Calculating Average Revenue Per Product Category
      val RevenuePerProductCategoryDf = revenueDf.withColumn("productCategory",substring(col("StockCode"), 1,3))

      val AvgRevenuePerProductCategoryDf = RevenuePerProductCategoryDf.groupBy("productCategory").agg(avg("revenue").as("revenuePerProductCategory"))

      AvgRevenuePerProductCategoryDf.coalesce(1).write.option("mergeSchema", "true").csv(s"${outputlocation}/AveragerevenuePerProductCategory")



      //6.Fetching Invoice Month
      val invoiceMonthDf = revenueDf.withColumn("InvoiceMonth",
        date_format(col("InvoiceDate"), "yyyy-MM"))
      invoiceMonthDf.coalesce(1).write.option("mergeSchema", "true").csv(s"${outputlocation}/InvoiceMonth")

      //7.Calculate Monthly revenue by invoice Month
      invoiceMonthDf.groupBy("InvoiceMonth").agg(sum("revenue").as("totalRevenueByInvoiceMonth")).coalesce(1).write.option("mergeSchema", "true").csv(s"${outputlocation}/revenueByInvoiceMonth")
    }

}
