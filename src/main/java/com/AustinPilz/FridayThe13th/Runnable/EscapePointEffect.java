package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena.EscapePoint;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class EscapePointEffect implements Runnable {

    private EscapePoint escapePoint;

    public EscapePointEffect(EscapePoint escapePoint) {
        this.escapePoint = escapePoint;
    }

    @Override
    public void run() {
        Firework f = escapePoint.getBoundary1().getWorld().spawn(escapePoint.getBoundary1().getWorld().getHighestBlockAt(escapePoint.getBoundary1()).getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.RED)
                .build());
        fm.setPower(1);
        f.setFireworkMeta(fm);

        Firework f2 = escapePoint.getBoundary1().getWorld().spawn(escapePoint.getBoundary1().getWorld().getHighestBlockAt(escapePoint.getBoundary2()).getLocation(), Firework.class);
        FireworkMeta fm2 = f2.getFireworkMeta();
        fm2.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.BLUE)
                .build());
        fm2.setPower(1);
        f2.setFireworkMeta(fm2);
    }
}
