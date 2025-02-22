package HerramientasSistema;

public final class BloqueDeCódigo {

    public interface Simple {

        void Ejecutar();
    }

    public interface Simple_Strings {

        void Ejecutar(String... s);
    }

    public interface Simple_Enteros {

        void Ejecutar(int... n);
    }

    public interface Simple_Longs {

        void Ejecutar(long... n);
    }
}
