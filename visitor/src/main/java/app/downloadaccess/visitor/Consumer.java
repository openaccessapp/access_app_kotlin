package app.downloadaccess.visitor;

public interface Consumer<T> {
    void accept(T t);
}
