package fr.redboxing.theylor.attributes.utils;

import com.google.common.collect.Maps;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class CooldownManager {
    private final Map<TheylorCompetence, Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(TheylorCompetence competence) {
        return this.getCooldownProgress(competence, 0.0F) > 0.0F;
    }

    public float getCooldownProgress(TheylorCompetence competence, float partialTicks) {
        Entry entry = this.entries.get(competence);
        if (entry != null) {
            float f = (float)(entry.endTick - entry.startTick);
            float g = (float)entry.endTick - ((float)this.tick + partialTicks);
            return MathHelper.clamp(g / f, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void update() {
        ++this.tick;
        if (!this.entries.isEmpty()) {
            Iterator iterator = this.entries.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<TheylorCompetence, Entry> entry = (Map.Entry<TheylorCompetence, Entry>)iterator.next();
                if (entry.getValue().endTick <= this.tick) {
                    iterator.remove();
                }
            }
        }

    }

    public void set(TheylorCompetence competence, int duration) {
        this.entries.put(competence, new Entry(this.tick, this.tick + duration));
    }

    public void remove(TheylorCompetence competence) {
        this.entries.remove(competence);
    }

    private static class Entry {
        final int startTick;
        final int endTick;

        Entry(int startTick, int endTick) {
            this.startTick = startTick;
            this.endTick = endTick;
        }
    }
}
