package aero;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/** Minimal SVG path-data parser (M L H V C S Q T A Z, absolute and relative). Subpath groups are separated by '|'. */
public final class SvgPath {
    private SvgPath() {}

    public static Path2D.Double parse(String data) {
        Path2D.Double out = new Path2D.Double(Path2D.WIND_NON_ZERO);
        for (String part : data.split("\\|")) parseOne(part, out);
        return out;
    }

    private static final class Sc {
        final String s; int p;
        Sc(String s) { this.s = s; }
        void ws() { while (p < s.length() && (Character.isWhitespace(s.charAt(p)) || s.charAt(p) == ',')) p++; }
        boolean more() { ws(); return p < s.length(); }
        boolean isNum() { ws(); if (p >= s.length()) return false; char c = s.charAt(p); return c == '-' || c == '+' || c == '.' || Character.isDigit(c); }
        double num() {
            ws(); int st = p;
            if (p < s.length() && (s.charAt(p) == '-' || s.charAt(p) == '+')) p++;
            boolean dot = false;
            while (p < s.length()) {
                char c = s.charAt(p);
                if (Character.isDigit(c)) p++;
                else if (c == '.' && !dot) { dot = true; p++; }
                else break;
            }
            if (p < s.length() && (s.charAt(p) == 'e' || s.charAt(p) == 'E')) { p++; if (s.charAt(p) == '-' || s.charAt(p) == '+') p++; while (p < s.length() && Character.isDigit(s.charAt(p))) p++; }
            return Double.parseDouble(s.substring(st, p));
        }
        boolean flag() { ws(); char c = s.charAt(p++); return c == '1'; }
    }

    private static void parseOne(String d, Path2D.Double path) {
        Sc sc = new Sc(d);
        double cx = 0, cy = 0, sx = 0, sy = 0, lcx = 0, lcy = 0; // current, subpath start, last control
        char cmd = 0, prev = 0;
        while (sc.more()) {
            char ch = d.charAt(sc.p);
            if (Character.isLetter(ch) && ch != 'e' && ch != 'E') { cmd = ch; sc.p++; } else if (cmd == 0) break;
            boolean rel = Character.isLowerCase(cmd);
            char up = Character.toUpperCase(cmd);
            switch (up) {
                case 'M': {
                    double x = sc.num(), y = sc.num(); if (rel) { x += cx; y += cy; }
                    path.moveTo(x, y); cx = sx = x; cy = sy = y; cmd = rel ? 'l' : 'L'; break; }
                case 'L': { double x = sc.num(), y = sc.num(); if (rel) { x += cx; y += cy; } path.lineTo(x, y); cx = x; cy = y; break; }
                case 'H': { double x = sc.num(); if (rel) x += cx; path.lineTo(x, cy); cx = x; break; }
                case 'V': { double y = sc.num(); if (rel) y += cy; path.lineTo(cx, y); cy = y; break; }
                case 'C': {
                    double x1 = sc.num(), y1 = sc.num(), x2 = sc.num(), y2 = sc.num(), x = sc.num(), y = sc.num();
                    if (rel) { x1 += cx; y1 += cy; x2 += cx; y2 += cy; x += cx; y += cy; }
                    path.curveTo(x1, y1, x2, y2, x, y); lcx = x2; lcy = y2; cx = x; cy = y; break; }
                case 'S': {
                    double x2 = sc.num(), y2 = sc.num(), x = sc.num(), y = sc.num();
                    if (rel) { x2 += cx; y2 += cy; x += cx; y += cy; }
                    double x1 = cx, y1 = cy; if (prev == 'C' || prev == 'S') { x1 = 2 * cx - lcx; y1 = 2 * cy - lcy; }
                    path.curveTo(x1, y1, x2, y2, x, y); lcx = x2; lcy = y2; cx = x; cy = y; break; }
                case 'Q': {
                    double x1 = sc.num(), y1 = sc.num(), x = sc.num(), y = sc.num();
                    if (rel) { x1 += cx; y1 += cy; x += cx; y += cy; }
                    path.quadTo(x1, y1, x, y); lcx = x1; lcy = y1; cx = x; cy = y; break; }
                case 'T': {
                    double x = sc.num(), y = sc.num(); if (rel) { x += cx; y += cy; }
                    double x1 = cx, y1 = cy; if (prev == 'Q' || prev == 'T') { x1 = 2 * cx - lcx; y1 = 2 * cy - lcy; }
                    path.quadTo(x1, y1, x, y); lcx = x1; lcy = y1; cx = x; cy = y; break; }
                case 'A': {
                    double rx = sc.num(), ry = sc.num(), rot = sc.num(); boolean large = sc.flag(), sweep = sc.flag();
                    double x = sc.num(), y = sc.num(); if (rel) { x += cx; y += cy; }
                    arc(path, cx, cy, rx, ry, rot, large, sweep, x, y); cx = x; cy = y; break; }
                case 'Z': path.closePath(); cx = sx; cy = sy; break;
                default: return;
            }
            prev = up;
            if (up == 'Z') { cmd = 0; if (!sc.more()) break; }
            else if (!sc.isNum() && sc.more() && !Character.isLetter(d.charAt(sc.p))) break;
        }
    }

    private static void arc(Path2D.Double path, double x0, double y0, double rx, double ry, double phiDeg, boolean fa, boolean fs, double x, double y) {
        if (rx == 0 || ry == 0 || (x0 == x && y0 == y)) { path.lineTo(x, y); return; }
        rx = Math.abs(rx); ry = Math.abs(ry);
        double phi = Math.toRadians(phiDeg), cp = Math.cos(phi), sp = Math.sin(phi);
        double dx = (x0 - x) / 2, dy = (y0 - y) / 2;
        double x1 = cp * dx + sp * dy, y1 = -sp * dx + cp * dy;
        double lam = x1 * x1 / (rx * rx) + y1 * y1 / (ry * ry);
        if (lam > 1) { double s = Math.sqrt(lam); rx *= s; ry *= s; }
        double num = rx * rx * ry * ry - rx * rx * y1 * y1 - ry * ry * x1 * x1;
        double den = rx * rx * y1 * y1 + ry * ry * x1 * x1;
        double co = Math.sqrt(Math.max(0, num / den)); if (fa == fs) co = -co;
        double cxp = co * rx * y1 / ry, cyp = -co * ry * x1 / rx;
        double cx = cp * cxp - sp * cyp + (x0 + x) / 2, cy = sp * cxp + cp * cyp + (y0 + y) / 2;
        double th1 = Math.atan2((y1 - cyp) / ry, (x1 - cxp) / rx);
        double th2 = Math.atan2((-y1 - cyp) / ry, (-x1 - cxp) / rx);
        double dth = th2 - th1;
        if (fs && dth < 0) dth += 2 * Math.PI; else if (!fs && dth > 0) dth -= 2 * Math.PI;
        int segs = (int) Math.ceil(Math.abs(dth) / (Math.PI / 2) - 1e-9);
        double step = dth / Math.max(1, segs);
        double t = 4.0 / 3.0 * Math.tan(step / 4);
        double a = th1;
        for (int i = 0; i < segs; i++) {
            double ca = Math.cos(a), sa = Math.sin(a), cb = Math.cos(a + step), sb = Math.sin(a + step);
            double[] p1 = {ca - t * sa, sa + t * ca}, p2 = {cb + t * sb, sb - t * cb}, p3 = {cb, sb};
            double[][] pts = {p1, p2, p3}; double[] o = new double[6];
            for (int k = 0; k < 3; k++) {
                double ex = pts[k][0] * rx, ey = pts[k][1] * ry;
                o[k * 2] = cp * ex - sp * ey + cx; o[k * 2 + 1] = sp * ex + cp * ey + cy;
            }
            path.curveTo(o[0], o[1], o[2], o[3], o[4], o[5]);
            a += step;
        }
    }
}
