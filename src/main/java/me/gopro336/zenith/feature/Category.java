package me.gopro336.zenith.feature;


public enum Category
{
	COMBAT("Combat"),
	EXPLOIT("Exploit"),
	RENDER("Render"),
	MOVEMENT("Movement"),
	MISC("Miscellaneous"),
	CLIENT("Client"),
	HUD("Client");

	private String name;

	Category(String name)
	{
		setName(name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
