package com.parzivail.parzidyne.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config extends Configuration
{
	public Config(File file)
	{
		super(file);
		load();
	}

	public void load()
	{
		super.load();

		// TODO: change to false when it's ready to be shown in a release
		// hasSeenIntroCrawlProp = get(Configuration.CATEGORY_GENERAL, "hasSeenIntroCrawl", true, "True if the user has seen the intro crawl before");

		if (hasChanged())
			save();
	}
}
