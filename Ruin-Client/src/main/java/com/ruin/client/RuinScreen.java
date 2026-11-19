package com.ruin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.*;

public class RuinScreen extends Screen {
    private final String[] categories = {"Interface", "Crystal PvP", "UHC", "Mace PvP", "SMP", "BedWars"};
    private int category = 0, scroll = 0;
    private String search = "";
    private boolean searching = false;
    private boolean themePicker = false;
    private static final int BG = 0xF20C0A15, PANEL = 0xFF1C1428, CARD = 0xFF261A34, WHITE = 0xFFF5EEFF, MUTED = 0xFFAB9CB9;
    public RuinScreen() { super(Text.literal("Ruin Client")); }
    @Override public boolean shouldPause() { return false; }
    @Override public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int x = Math.max(10, (width - 750) / 2), y = Math.max(10, (height - 440) / 2);
        int w = Math.min(750, width - 20), h = Math.min(440, height - 20);
        ctx.fill(x, y, x+w, y+h, BG);
        ctx.fill(x, y, x+w, y+3, RuinClient.accent());
        ctx.drawTextWithShadow(textRenderer, "R  RUIN CLIENT", x+18, y+17, WHITE);
        ctx.drawTextWithShadow(textRenderer, "v0.1.0", x+143, y+17, MUTED);
        ctx.drawTextWithShadow(textRenderer, "MODULES", x+215, y+17, RuinClient.accent());
        ctx.drawTextWithShadow(textRenderer, "THEMES", x+312, y+17, WHITE);
        ctx.drawTextWithShadow(textRenderer, "HOME", x+403, y+17, WHITE);
        ctx.fill(x+w-205, y+10, x+w-18, y+32, PANEL);
        ctx.drawTextWithShadow(textRenderer, (searching ? "> " : "Search: ")+search+(searching ? "_" : ""), x+w-195, y+17, MUTED);
        ctx.fill(x+12, y+46, x+184, y+h-12, PANEL);
        for (int i=0; i<categories.length; i++) {
            int cy=y+58+i*42;
            if (i==category) ctx.fill(x+20, cy-7, x+176, cy+25, RuinClient.accent());
            ctx.drawTextWithShadow(textRenderer, categories[i], x+35, cy+3, WHITE);
        }
        ctx.drawTextWithShadow(textRenderer, "RSHIFT  /  ESC  TO CLOSE", x+24, y+h-32, MUTED);
        int sx=x+200, sy=y+52;
        if (themePicker) {
            ctx.drawTextWithShadow(textRenderer, "THEME MANAGER", sx+10, sy, WHITE);
            for (int i=0;i<RuinClient.THEMES.length;i++) {
                int tx=sx+(i%2)*260, ty=sy+28+(i/2)*75;
                ctx.fill(tx,ty,tx+248,ty+62,CARD);
                ctx.fill(tx,ty,tx+5,ty+62, i==RuinClient.theme?RuinClient.accent():MUTED);
                ctx.drawTextWithShadow(textRenderer,RuinClient.THEMES[i]+(i==RuinClient.theme?"  [ACTIVE]":""),tx+22,ty+24,WHITE);
            }
        } else {
            ctx.drawTextWithShadow(textRenderer, categories[category].toUpperCase()+" MODULES", sx+8, sy, WHITE);
            List<RuinClient.Module> visible = filtered();
            int cols=2, cellW=Math.max(120,(w-225)/cols), cellH=48;
            int maxRows=Math.max(1,(h-112)/cellH);
            scroll=Math.max(0,Math.min(scroll,Math.max(0,(visible.size()+1)/2-maxRows)));
            for (int i=scroll*2;i<visible.size() && i<(scroll+maxRows)*2;i++) {
                RuinClient.Module m=visible.get(i);
                int cx=sx+((i-scroll*2)%2)*cellW, cy=sy+24+((i-scroll*2)/2)*cellH;
                ctx.fill(cx,cy,cx+cellW-8,cy+41,CARD);
                ctx.drawTextWithShadow(textRenderer,m.name,cx+12,cy+15,WHITE);
                ctx.fill(cx+cellW-47,cy+12,cx+cellW-18,cy+29,m.enabled?RuinClient.accent():0xFF5A4C66);
                ctx.drawTextWithShadow(textRenderer,m.enabled?"ON":"OFF",cx+cellW-43,cy+16,WHITE);
            }
            ctx.drawTextWithShadow(textRenderer,"Registered modules are UI placeholders until implemented.",sx+5,y+h-24,MUTED);
        }
    }
    private List<RuinClient.Module> filtered() {
        return RuinClient.MODULES.stream().filter(m->m.category.equals(categories[category]) && m.name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))).toList();
    }
    @Override public boolean mouseClicked(double mx,double my,int button) {
        int x=Math.max(10,(width-750)/2), y=Math.max(10,(height-440)/2), w=Math.min(750,width-20), h=Math.min(440,height-20);
        if (my>=y+10 && my<=y+36) {
            if (mx>=x+300 && mx<x+385) { themePicker=true; return true; }
            if (mx>=x+395 && mx<x+465) { client.setScreen(new RuinHomeScreen()); return true; }
            if (mx>=x+210 && mx<x+290) { themePicker=false; return true; }
            if (mx>=x+w-205 && mx<x+w-18) { searching=true; return true; }
        }
        if (mx>=x+12 && mx<x+184) {
            for (int i=0;i<categories.length;i++) if (my>=y+51+i*42 && my<y+83+i*42) { category=i;scroll=0;themePicker=false;return true; }
        }
        int sx=x+200, sy=y+52;
        if (themePicker) {
            for(int i=0;i<RuinClient.THEMES.length;i++) {
                int tx=sx+(i%2)*260,ty=sy+28+(i/2)*75;
                if(mx>=tx && mx<tx+248 && my>=ty && my<ty+62) {RuinClient.theme=i;RuinClient.save();return true;}
            }
        } else {
            int cellW=Math.max(120,(w-225)/2), maxRows=Math.max(1,(h-112)/48);
            List<RuinClient.Module> visible=filtered();
            for(int i=scroll*2;i<visible.size()&&i<(scroll+maxRows)*2;i++) {
                int cx=sx+((i-scroll*2)%2)*cellW,cy=sy+24+((i-scroll*2)/2)*48;
                if(mx>=cx&&mx<cx+cellW-8&&my>=cy&&my<cy+41) {visible.get(i).enabled=!visible.get(i).enabled;RuinClient.save();return true;}
            }
        }
        searching=false;
        return super.mouseClicked(mx,my,button);
    }
    @Override public boolean mouseScrolled(double mx,double my,double horizontal,double vertical) { scroll=Math.max(0,scroll-(int)Math.signum(vertical));return true; }
    @Override public boolean charTyped(char chr,int modifiers) {if(searching && !Character.isISOControl(chr)){search+=chr;scroll=0;return true;}return super.charTyped(chr,modifiers);}
    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers) {
        if(searching){if(keyCode==259 && !search.isEmpty()){search=search.substring(0,search.length()-1);scroll=0;return true;}if(keyCode==257){searching=false;return true;}}
        return super.keyPressed(keyCode,scanCode,modifiers);
    }
}
