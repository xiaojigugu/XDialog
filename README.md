# XDialog
[![](https://jitpack.io/v/com.gitee.giteeguguji/XDialog.svg)](https://jitpack.io/#com.gitee.giteeguguji/XDialog)
#### 介绍 
简单易用的Dialog封装  
<img src="https://gitee.com/giteeguguji/XDialog/raw/master/app/src/main/images/sample.gif" width="30%" height="30%">

#### 软件架构 
基于Dialog进行二次封装  
Dialog->PhoneWindow->DecorView->自定义Dialog容器Layout

#### 使用说明

Add it in your root build.gradle at the end of repositories:  
``  
    allprojects {  
        repositories {  
			...  
			maven { url 'https://jitpack.io' }  
		}  
	}  
``  
Step 2. Add the dependency  
``  
	dependencies {  
	        implementation 'com.gitee.giteeguguji:XDialog:v1.0'  
	}  
``  