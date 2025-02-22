package HerramientaDeImagen.ModoFusión;

import HerramientaDeImagen.ModoFusión.ModoFusión.Conmutar.Normal;
import HerramientaDeImagen.ModoFusión.ModoPintura.BaseModoPintura;
import java.util.ArrayList;

public final class ModoPinturaCompuesto extends Normal {

    public ArrayList<BaseModoPintura> ModosPintura = new ArrayList<>();

    public ModoPinturaCompuesto(BaseModoPintura... mPintura) {//<editor-fold defaultstate="collapsed" desc="Implementación de constructor »">
        if (mPintura != null) {
            for (BaseModoPintura baseModoPintura : mPintura) {
                ModosPintura.add(baseModoPintura);
            }
        }
        RiesgoSalida = false;
    }//</editor-fold>

    @Override
    public void ModificarPixelesPintar(int[] Pintar, int P) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (BaseModoPintura baseModoPintura : ModosPintura) {
            baseModoPintura.ModificarPixelesPintar(Pintar, P);
        }
        super.ModificarPixelesPintar(Pintar, P);
    }//</editor-fold>

}
