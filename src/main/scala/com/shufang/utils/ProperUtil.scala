package com.shufang.utils

import java.util.Properties

import org.apache.commons.configuration2.{FileBasedConfiguration, PropertiesConfiguration}
import org.apache.commons.configuration2.builder.{BasicBuilderParameters, FileBasedConfigurationBuilder, PropertiesBuilderParametersImpl}
import org.apache.commons.configuration2.builder.fluent.{Parameters, PropertiesBuilderParameters}

/**
 * 这是一个非常实用的读取配置文件的类
 * 首先得导入相关的依赖apache
 * <!--很好用的apache的工具类包-->
 * <dependency>
 * <groupId>org.apache.commons</groupId>
 * <artifactId>commons-configuration2</artifactId>
 * <version>2.2</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>commons-beanutils</groupId>
 * <artifactId>commons-beanutils</artifactId>
 * <version>1.9.3</version>
 * </dependency>
 *
 */
object ProperUtil {
//  //apply方法可以用来初始化
//  def apply(propertiesFile: String): ProperUtil = {
//    val util: ProperUtil = new ProperUtil
//    if (util.fileBasedConfiguration == null) {
//      util.fileBasedConfiguration =
//        new FileBasedConfigurationBuilder[FileBasedConfiguration](classOf[PropertiesConfiguration])
//          .configure(
//            new Parameters()
//              .properties()
//              .setFileName(propertiesFile)
//          ).getConfiguration
//    }
//    util
//  }
//
//
//  def main(args: Array[String]): Unit = {
//    val parameters: PropertiesBuilderParameters = new Parameters().properties()
//    parameters.setBasePath("/")
//    val configuration: FileBasedConfiguration = new FileBasedConfigurationBuilder[FileBasedConfiguration](classOf[PropertiesConfiguration])
//      .configure(
//        parameters
//      ).getConfiguration
//
//    println(configuration)
//  }
}

class ProperUtil {
  var fileBasedConfiguration: FileBasedConfiguration = _
}
