package com.AustinPilz.FridayThe13th.IO;


import java.util.List;


public class Settings {
	
	public Settings()
	{
		//
	}
	
	public static Object getGlobalProperty(Setting setting)
	{
		Object property = InputOutput.global.get(setting.getString());
		if (property == null)
		{
			property = setting.getDefault();
		}
		
		return property;
	}

	public static Boolean getGlobalBoolean(Setting setting)
	{
		return 	(Boolean) getGlobalProperty(setting);
	}
	
	public static Integer getGlobalInt(Setting setting)
	{
		return 	(Integer) getGlobalProperty(setting);
	}

	public static String getGlobalString(Setting setting)
	{
		return 	(String) getGlobalProperty(setting);
	}
	
	public static List<?> getGlobalList(Setting setting)
	{
		return 	(List<?>) getGlobalProperty(setting);
	}
}


