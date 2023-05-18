# Полноэкранная реклама с вознаграждением
Видео, после просмотра которого пользователю можно выдать награду.

## Загрузка рекламы
Для загрузки рекламы используйте следующий код
```java
Madex.loadAd(activity, Madex.REWARDED);
```

## Методы обратного вызова
Для обработки событий жизненного цикла необходимо предоставить класс для работы.
```java
Madex.setRewardedListener(new MadexRewardedListener(){
    @Override
    public void onRewardedLoaded(){
        // Вызывется при загрузке рекламы
    }

    @Override
    public void onRewardedLoadFail(String message){
        // Вызывется если при загрузке рекламы произошла ошибка
    }

    @Override
    public void onRewardedShown(){
        // Вызывается при показе рекламы
    }

    @Override
    public void onRewardedShowFailed(String message){
        // Вызывется если при показе рекламы произошла ошибка
    }

    @Override
    public void onRewardedClosed(){
        // Вызывается при закрытии рекламы
    }

    @Override
    public void onRewardedFinished(){
        // Вызывется когда реклама закончилась
    }
}
```

## Проверка загрузки рекламы
Вы можете проверить статус загрузки перед работы с рекламой.
```java
Madex.isAdLoaded(Madex.REWARDED);
```

Рекомендуем всегда проверять статус загрузки рекламы, прежде чем пытаться ее показать.
```java
if(Madex.isAdLoaded(Madex.REWARDED)) {
    Madex.showAd(activity, Madex.REWARDED);
}
```

## Показ рекламы
Для показа рекламы используйте метод:
```java
Madex.showAd(activity, Madex.REWARDED);
```

## Уничтожение рекламного контейнера
Для уничтожения рекламы добавьте следующий код в вашем приложении.
```java
Madex.destroyAd(Madex.REWARDED);
```