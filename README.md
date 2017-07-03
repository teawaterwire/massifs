# :trophy: Le jeu des Massifs

Une appli [re-frame](https://github.com/Day8/re-frame) pour apprendre à placer les massifs des Alpes occidentales.

**Impatients ?** [JOUER MAINTENANT !](https://massifs.booctin.com)

## :warning: Achtung
Pour une fois j'avais envie de faire du :fr: *French first* – sorry mates!

Je me suis aussi laissé aller sur les messages des *commits* – un besoin d'évasion évident :metal:

## Pour faire tourner ça chez :house_with_garden: vous

Le classique :

```
lein clean
lein figwheel dev
```

Figwheel va faire sa petite :stew: tambouille...

...et il vous restera à aller sur [http://localhost:3449](http://localhost:3449).


## Pour faire tourner ça dans le :cloud: nuage


On crache le `js` à partir du `cljs` :

```
lein clean
lein cljsbuild once min
```

## :raised_hands: Crédits

Merci à Wikipedia pour :sunrise_over_mountains: [les données des massifs](https://fr.wikipedia.org/wiki/G%C3%A9ographie_des_Alpes).
