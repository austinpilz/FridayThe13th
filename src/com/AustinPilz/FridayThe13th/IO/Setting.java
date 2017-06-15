package com.AustinPilz.FridayThe13th.IO;

public enum Setting {

	//Update
	CheckForUpdates("CheckForUpdates", true),
	NotifyOnNewUpdates("NotifyOnNewUpdates", true),
	ReportMetrics("MetricReporting",true),
	NotifyOnAustinPilz("NotifyOnPluginCreatorJoin", true),
	
	//GamePlay
	gameplayMaxStamina("Gameplay.MaxStamina", 30);
	
	
	
	
	private String name;
	private Object def;
	
	private Setting(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getString()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
}

