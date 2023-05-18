# Полноэкранная реклама
Баннер на весь экран, который можно закрыть через несколько секунд.

## Загрузка рекламы
Для загрузки рекламы используйте следующий код
```java
Madex.loadAd(activity, Madex.INTERSTITIAL);
```

## Методы обратного вызова
Для обработки событий жизненного цикла необходимо предоставить класс для работы.
```java
Madex.setInterstitialListener(new MadexInterstitialListener(){
    @Override
    public void onInterstitialLoaded(){
        // Вызывется при загрузке рекламы
    }
    
    @Override
    public void onInterstitialLoadFail(String message){
        // Вызывется если при загрузке рекламы произошла ошибка
    }
    
    @Override
    public void onInterstitialShown(){
        // Вызывается при показе рекламы
    }
    
    @Override
    public void onInterstitialShowFailed(String message){
       // Вызывется если при показе рекламы произошла ошибка
    }
    
    @Override
    public void onInterstitialClosed(){
        // Вызывается при закрытии рекламы
    }
}
```

## Проверка загрузки рекламы
Вы можете проверить статус загрузки перед работы с рекламой.
```java
Madex.isAdLoaded(Madex.INTERSTITIAL);
```

Рекомендуем всегда проверять статус загрузки рекламы, прежде чем пытаться ее показать.
```java
if(Madex.isAdLoaded(Madex.INTERSTITIAL)) {
    Madex.showAd(activity, Madex.INTERSTITIAL);
}
```

## Показ рекламы
Для показа рекламы используйте метод:
```java
Madex.showAd(activity, Madex.INTERSTITIAL);
```

## Уничтожение рекламного контейнера
Для уничтожения рекламы добавьте следующий код в вашем приложении.
```java
Madex.destroyAd(Madex.INTERSTITIAL);
```