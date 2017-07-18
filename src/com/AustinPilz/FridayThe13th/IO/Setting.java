package com.AustinPilz.FridayThe13th.IO;

public enum Setting {

	//Update
	CheckForUpdates("CheckForUpdates", true),
	NotifyOnNewUpdates("NotifyOnNewUpdates", true),
	ReportMetrics("MetricReporting",true),
	NotifyOnAustinPilz("NotifyOnPluginCreatorJoin", true),
	
	//GamePlay
	gameplayMaxStamina("Gameplay.MaxStamina", 30),
	gameplayWaitingTime("Gameplay.WaitingTimeInSeconds", 20),
	gameplayGameTime("Gameplay.GameTimeInSeconds", 600),
	gameplayWarnOnBreak("Gameplay.Alerts.AlertOnBlockBreakAttempt", true),
	gameplayWarnOnPlace("Gameplay.Alerts.AlertOnBlockPlaceAttempt", true);
	
	
	
	
	private String name;
	private Object def;

	Setting(String Name, Object Def)
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

