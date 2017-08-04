package com.AustinPilz.FridayThe13th.Components.Skin;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Skin;

public interface SkinChange {
    public void apply(F13Skin skin);

    public void revert();
}
