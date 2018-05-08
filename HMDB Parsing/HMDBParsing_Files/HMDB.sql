# for extracting the desired metabolites from hmdb.db
SELECT distinct meta.metabolitesName, meta.metabolitesSMILES from HMDBmetabolites meta, HMDBrelation where  meta.metabolitesID in(
SELECT HMDBrelation.metabolitesID FROM HMDBrelation where HMDBrelation.transporterID = 5684);