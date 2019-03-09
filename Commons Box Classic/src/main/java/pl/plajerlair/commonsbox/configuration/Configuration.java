package pl.plajerlair.commonsbox.configuration;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public interface Configuration<E> {

    E getConfiguration(String file);

}
